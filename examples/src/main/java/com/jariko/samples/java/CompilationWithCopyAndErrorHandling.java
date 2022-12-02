/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jariko.samples.java;

import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.JarikoCallback;
import com.smeup.rpgparser.execution.Options;
import com.smeup.rpgparser.interpreter.RpgProgram;
import com.smeup.rpgparser.parsing.ast.Api;
import com.smeup.rpgparser.parsing.ast.ApiDescriptor;
import com.smeup.rpgparser.parsing.ast.ApiId;
import com.smeup.rpgparser.parsing.facade.Copy;
import com.smeup.rpgparser.parsing.facade.CopyId;
import com.smeup.rpgparser.parsing.facade.SourceReferenceType;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;
import com.smeup.rpgparser.utils.Format;
import com.smeup.rpgparser.utils.RpgcompilerKt;
import com.strumenta.kolasu.model.Position;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompilationWithCopyAndErrorHandling {

    private static class MyProgramFinder implements RpgProgramFinder {
        @Override
        public RpgProgram findRpgProgram(@NotNull String name) {
            throw new UnsupportedOperationException("implementation not necessary");
        }


        @Override
        public Copy findCopy(@NotNull CopyId copyId) {
            // copy are located in /rpg/copyId.getFile() directory
            final Path copyPath = Paths.get("/rpg", copyId.getFile(), copyId.getMember() + ".rpgle");
            try (InputStream in = getClass().getResourceAsStream(copyPath.toString().replace('\\', '/'))) {
                return Copy.Companion.fromInputStream(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        @Override
        public ApiDescriptor findApiDescriptor(@NotNull ApiId apiId) {
            throw new UnsupportedOperationException("implementation not necessary");
        }

        @Override
        public Api findApi(@NotNull ApiId apiId) {
            throw new UnsupportedOperationException("implementation not necessary");
        }
    }

    public static void main(String[] args) throws IOException{

        final Configuration configuration = new Configuration();
        final Options options = new Options();
        // Use this option to get compilation error
        options.setDebuggingInformation(true);
        configuration.setOptions(options);
        final JarikoCallback callback = new JarikoCallback();
        configuration.setJarikoCallback(callback);
        callback.setOnError(errorEvent -> {
            // Here, you can handle the compilation errors
            // for example I log in system error

            // The error message
            final String errorMessage = errorEvent.getError().getMessage();

            // Program or copy name
            final String sourceId = errorEvent.getSourceReference().getSourceId();
            final Position errorPosition = errorEvent.getSourceReference().getPosition();
            System.err.println("Error: " + errorMessage + " occurred in: " + sourceId + " at position: " + errorPosition);
            return null;
        });
        // Create a collection of program finders
        final List<RpgProgramFinder> programFinders = Collections.singletonList(new MyProgramFinder());

        // Compile
        try (InputStream in = JarikoCompilingSample.class.getResourceAsStream("/rpg/CALL_CPY01.rpgle")) {
            RpgcompilerKt.compile(in, new ByteArrayOutputStream(), Format.BIN, false, programFinders, configuration);
        } catch(IllegalStateException e) {
        // I don't throw an exception because I handle error through the callback
        System.err.println("Warning: " + e.getMessage());
    }
    }
}
