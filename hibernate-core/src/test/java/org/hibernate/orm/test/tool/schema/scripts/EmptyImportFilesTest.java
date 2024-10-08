/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.tool.schema.scripts;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import org.hibernate.testing.orm.junit.JiraKey;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * @author Vlad Mihalcea
 */
@JiraKey(value = "HHH-13089")
public class EmptyImportFilesTest extends BaseCoreFunctionalTestCase {

	@Override
	public void configure(Configuration cfg) {
		cfg.setProperty(
				Environment.HBM2DDL_IMPORT_FILES,
				""
		);
	}

	@Test
	public void testImportFile() throws Exception {
	}
}
