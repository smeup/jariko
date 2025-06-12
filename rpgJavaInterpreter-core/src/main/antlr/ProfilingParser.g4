parser grammar ProfilingParser;

options { tokenVocab = ProfilingLexer; }

profilingLine: profilingAnnotation EOF;

profilingAnnotation: TELEMETRY_SPAN_START TELEMETRY_SPAN_ID TELEMETRY_SPAN_COMMENT? #profilingSpanStartAnnotation
                   | TELEMETRY_SPAN_END #profilingSpanEndAnnotation
                   ;