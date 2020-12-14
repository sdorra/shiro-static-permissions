/*
 * The MIT License
 *
 * Copyright 2016 Sebastian Sdorra.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package com.github.sdorra.ssp;

import com.github.sdorra.shiro.ShiroRule;
import com.github.sdorra.shiro.SubjectAware;
import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SubjectAware(
    username = "trillian",
    password = "secret",
    configuration = "classpath:com/github/sdorra/ssp/shiro.ini"
)
public class RepositoryPermissionsTest {

    @Rule
    public ShiroRule rule = new ShiroRule();

    @Test
    public void testIsPermitted() {
        assertFalse(RepositoryPermissions.read("123").isPermitted());
        assertFalse(RepositoryPermissions.modify("123").isPermitted());
        assertFalse(RepositoryPermissions.delete("123").isPermitted());

        assertTrue(RepositoryPermissions.delete(new Repository("1234")).isPermitted());
        assertTrue(RepositoryPermissions.delete("1234").isPermitted());
        assertTrue(RepositoryPermissions.create().isPermitted());
    }

    @Test
    public void testCustomGlobal() {
        assertFalse(RepositoryPermissions.custom("special").isPermitted());

        assertTrue(RepositoryPermissions.custom("fork").isPermitted());
    }

    @Test
    public void testCustom() {
        assertFalse(RepositoryPermissions.custom("merge", "1235").isPermitted());

        assertTrue(RepositoryPermissions.custom("merge", "1234").isPermitted());
    }

    @Test
    public void testInterceptor() {
        assertFalse(RepositoryPermissions.delete("mustNotBeDeleted").isPermitted());
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheck() {
        RepositoryPermissions.read("123").check();
    }
}
