package org.idempiere.common.util;

/**
 * Amount in Words for Malaysian Ringgit (RM)
 *
 * @author Jorg Janke - http://www.rgagnon.com/javadetails/java-0426.html
 * @contributor red1 - http://red1.org NOTE: Locally practiced words as RINGGIT preceding dollars
 *     dan SEN (and CENTS) before cents in full words. Each comma is noted in wordings too with
 *     'dan' denotes decimal point. This is yet not considered perfect as Satu Ratus (One Hundred)
 *     is often stated as Seratus (A Hundred) and so is it with Seribu (A Thousand) and Sejuta (A
 *     Million), but the longer is common practice and idiot proof. TEST RESULT: 0.23 = RINGGIT
 *     Kosong dan SEN Dua Puluh Tiga 1.23 = RINGGIT Satu dan SEN Dua Puluh Tiga 12.34 = RINGGIT Dua
 *     Belas dan SEN Tiga Puluh Empat 123.45 = RINGGIT Satu Ratus Dua Puluh Tiga dan SEN Empat Puluh
 *     Lima 1,234.56 = RINGGIT Satu Ribu, Dua Ratus Tiga Puluh Empat dan SEN Lima Puluh Enam
 *     12,345.78 = RINGGIT Dua Belas Ribu, Tiga Ratus Empat Puluh Lima dan SEN Tujuh Puluh Lapan
 *     123,457.89 = RINGGIT Satu Ratus Dua Puluh Tiga Ribu, Empat Ratus Lima Puluh Tujuh dan SEN
 *     Lapan Puluh Sembilan 1,234,578.90 = RINGGIT Satu Juta, Dua Ratus Tiga Puluh Empat Ribu, Lima
 *     Ratus Tujuh Puluh Lapan dan SEN Sembilan Puluh 100.00 = RINGGIT Satu Ratus dan SEN Kosong END
 *     TEST
 * @version $Id: AmtInWords_MY.java,v 1.3 2006/07/30 00:54:35 jjanke Exp $
 */
public class AmtInWords_MS implements AmtInWords {
  /** AmtInWords_MY */
  public AmtInWords_MS() {
    super();
  } //	AmtInWords_MY

  /** Thousands plus */
  private static final String[] majorNames = {
    "", "Ribu, ", "Juta, ", "Bilion, ", "Trilion, ", "Quadrillion, ", "Quintillion, "
  };

  /** Ten to Ninety */
  private static final String[] tensNames = {
    "",
    "Sepuluh ",
    "Dua Puluh ",
    "Tiga Puluh ",
    "Empat Puluh ",
    "Lima Puluh ",
    "Enam Puluh ",
    "Tujuh Puluh ",
    "Lapan Puluh ",
    "Sembilan Puluh "
  };

  /** numbers to 19 */
  private static final String[] numNames = {
    "",
    "Satu ",
    "Dua ",
    "Tiga ",
    "Empat ",
    "Lima ",
    "Enam ",
    "Tujuh ",
    "Lapan ",
    "Sembilan ",
    "Sepuluh ",
    "Sebelas",
    "Dua Belas ",
    "Tiga Belas ",
    "Empat Belas ",
    "Lima Belas ",
    "Enam Belas ",
    "Tujuh Belas ",
    "Lapan Belas ",
    "Sembilan Belas "
  };

  /**
   * Convert Less Than One Thousand
   *
   * @param number
   * @return amt
   */
  private String convertLessThanOneThousand(int number) {
    String soFar;
    //	Below 20
    if (number % 100 < 20) {
      soFar = numNames[number % 100];
      number /= 100;
    } else {
      soFar = numNames[number % 10];
      number /= 10;
      soFar = tensNames[number % 10] + soFar;
      number /= 10;
    }
    if (number == 0) return soFar;
    return numNames[number] + "Ratus " + soFar;
  } //	convertLessThanOneThousand

  /**
   * Convert
   *
   * @param number
   * @return amt
   */
  private String convert(long number) {
    /* special case */
    if (number == 0) {
      return "Kosong";
    }
    String prefix = "";
    if (number < 0) {
      number = -number;
      prefix = "Negatif ";
    }
    String soFar = "";
    int place = 0;
    do {
      long n = number % 1000;
      if (n != 0) {
        String s = convertLessThanOneThousand((int) n);
        soFar = s + majorNames[place] + soFar;
      }
      place++;
      number /= 1000;
    } while (number > 0);
    return (prefix + soFar).trim();
  } //	convert

  /**
   * ************************************************************************ Get Amount in Words
   *
   * @param amount numeric amount (352.80)
   * @return amount in words (three*five*two 80/100)
   * @throws Exception
   */
  public String getAmtInWords(String amount) throws Exception {
    if (amount == null) return amount;
    //
    StringBuilder sb = new StringBuilder();
    sb.append("RINGGIT ");
    int pos = amount.lastIndexOf('.');
    int pos2 = amount.lastIndexOf(',');
    if (pos2 > pos) pos = pos2;
    String oldamt = amount;
    amount = amount.replaceAll(",", "");
    int newpos = amount.lastIndexOf('.');
    long dollars = Long.parseLong(amount.substring(0, newpos));
    sb.append(convert(dollars));
    for (int i = 0; i < oldamt.length(); i++) {
      if (pos == i) // 	we are done
      {
        String cents = oldamt.substring(i + 1);
        long sen = Long.parseLong(cents); // red1 convert cents to words
        sb.append(" dan SEN");
        sb.append(' ').append(convert(sen));
        break;
      }
    }
    return sb.toString();
  } //	getAmtInWords

  /**
   * Test Print
   *
   * @param amt amount
   */
  private void print(String amt) {
    try {
      System.out.println(amt + " = " + getAmtInWords(amt));
    } catch (Exception e) {
      e.printStackTrace();
    }
  } //	print
} //	AmtInWords_MY
