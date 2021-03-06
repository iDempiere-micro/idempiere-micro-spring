package org.idempiere.common.util;

import java.util.ListResourceBundle;

/**
 * License Dialog Translation
 *
 * @author Kyriakos Ioannidis - kioa@openway.gr
 * @version $Id: IniRes.java,v 1.2 2006/07/30 00:52:23 jjanke Exp $
 */
public class IniRes_el extends ListResourceBundle {
  /** Translation Content */
  static final Object[][] contents =
      new String[][] {
        {
          "Adempiere_License",
          "\u03a3\u03c5\u03bc\u03c6\u03c9\u03bd\u03af\u03b1 \u0386\u03b4\u03b5\u03b9\u03b1\u03c2 \u03a7\u03c1\u03ae\u03c3\u03b7\u03c2"
        },
        {
          "Do_you_accept",
          "\u0391\u03c0\u03bf\u03b4\u03ad\u03c7\u03b5\u03c3\u03c4\u03b5 \u03c4\u03b7\u03bd \u0386\u03b4\u03b5\u03b9\u03b1 \u03a7\u03c1\u03ae\u03c3\u03b7\u03c2 ?"
        },
        {"No", "\u038c\u03c7\u03b9"},
        {
          "Yes_I_Understand",
          "\u039d\u03b1\u03b9, \u03c4\u03b7\u03bd \u03ba\u03b1\u03c4\u03ac\u03bb\u03b1\u03b2\u03b1 \u03ba\u03b1\u03b9 \u03c4\u03b7\u03bd \u0391\u03c0\u03bf\u03b4\u03ad\u03c7\u03bf\u03bc\u03b1\u03b9"
        },
        {"license_htm", "org/idempiere/license.htm"},
        {
          "License_rejected",
          "\u0397 \u0386\u03b4\u03b5\u03b9\u03b1 \u03a7\u03c1\u03ae\u03c3\u03b7\u03c2 \u03b1\u03c0\u03bf\u03c1\u03c1\u03af\u03c6\u03b8\u03b7\u03ba\u03b5 \u03ae \u03ad\u03bb\u03b7\u03be\u03b5"
        }
      };

  /**
   * Get Content
   *
   * @return Content
   */
  public Object[][] getContents() {
    return contents;
  } //  getContent
} //  IniRes
