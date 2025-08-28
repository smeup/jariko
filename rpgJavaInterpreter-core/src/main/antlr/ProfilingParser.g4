parser grammar ProfilingParser;

options { tokenVocab = ProfilingLexer; }

profilingLine: profilingAnnotation EOF;

profilingAnnotation: TELEMETRY_SPAN_START TELEMETRY_SPAN_ID_SCOPE OPEN_PAREN TELEMETRY_SPAN_ID CLOSE_PAREN TELEMETRY_SPAN_COMMENT? #profilingSpanStartAnnotation
                   | TELEMETRY_SPAN_END (TELEMETRY_SPAN_ID_SCOPE OPEN_PAREN TELEMETRY_SPAN_ID CLOSE_PAREN)? captureScope? TELEMETRY_SPAN_COMMENT? #profilingSpanEndAnnotation
                   ;

attributeCapture: TELEMETRY_SPAN_ATTRIBUTE_SCOPE OPEN_PAREN TELEMETRY_SPAN_ID CLOSE_PAREN;

captureScope: TELEMETRY_SPAN_CAPTURE_SCOPE OPEN_PAREN TELEMETRY_XXX_SPLAT WS* attributeCapture (SEMICOLON attributeCapture)*;