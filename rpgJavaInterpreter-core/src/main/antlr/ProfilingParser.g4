parser grammar ProfilingParser;

options { tokenVocab = ProfilingLexer; }

profilingLine: profilingAnnotation EOF;

profilingAnnotation: TELEMETRY_SPAN_START TELEMETRY_SPAN_ID_SCOPE OPEN_PAREN TELEMETRY_SPAN_ID CLOSE_PAREN TELEMETRY_SPAN_COMMENT? #profilingSpanStartAnnotation
                   | TELEMETRY_SPAN_END (TELEMETRY_SPAN_ID_SCOPE OPEN_PAREN TELEMETRY_SPAN_ID CLOSE_PAREN)? TELEMETRY_SPAN_COMMENT? #profilingSpanEndAnnotation
                   ;