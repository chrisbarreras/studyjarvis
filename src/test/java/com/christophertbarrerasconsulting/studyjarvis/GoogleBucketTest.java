package com.christophertbarrerasconsulting.studyjarvis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GoogleBucketTest {

    @Test
    void sanitizeForGcsUri_replacesSpaces() {
        assertEquals("FUS_SFE_Intro.pptx", GoogleBucket.sanitizeForGcsUri("FUS SFE Intro.pptx"));
    }

    @Test
    void sanitizeForGcsUri_replacesColons() {
        assertEquals("session_1_1.png", GoogleBucket.sanitizeForGcsUri("session:1:1.png"));
    }

    @Test
    void sanitizeForGcsUri_preservesForwardSlashesForSubpaths() {
        assertEquals("sub/dir/file.txt", GoogleBucket.sanitizeForGcsUri("sub/dir/file.txt"));
    }

    @Test
    void sanitizeForGcsUri_preservesDotDashUnderscore() {
        assertEquals("a.b-c_d.png", GoogleBucket.sanitizeForGcsUri("a.b-c_d.png"));
    }

    @Test
    void sanitizeForGcsUri_replacesOtherUriReservedChars() {
        // Characters that have special meaning in URIs (or are plain disallowed)
        // and would break Vertex AI's gs:// URI parser.
        assertEquals("a_b_c_d_e_f_g_h_i_j", GoogleBucket.sanitizeForGcsUri("a b:c?d#e[f]g@h!i*j"));
    }

    @Test
    void sanitizeForGcsUri_realisticBuggyFilename() {
        // Regression case: the exact filename shape that produced
        // INVALID_ARGUMENT errors from Vertex AI before this sanitization.
        String sanitized = GoogleBucket.sanitizeForGcsUri("user 1:1 1 Arch Quiz 2 All Slides.png");
        assertFalse(sanitized.contains(" "), "spaces must be stripped");
        assertFalse(sanitized.contains(":"), "colons must be stripped");
    }
}
