package com.example.openinvitetest;
import android.content.Context;
import org.junit.Test;

import com.google.common.truth.Truth;

public class unitTest {
    private static final String userID = "abc";

    @Test
    public void readStringFromContext_LocalizedString() {
        // Given a Context object retrieved from Robolectric...
        userIdViewModel myObjectUnderTest = new userIdViewModel();

        // ...when the string is returned from the object under test...
        myObjectUnderTest.setUserID(userID);

        // ...then the result should be the expected one.
        Truth.assertThat("abc").isEqualTo(myObjectUnderTest.returnID());
    }
}