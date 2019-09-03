package animal.exchange;

/**
 * This interface offers standard methods for format generation.
 * The methods cover the default extension, MIME type, and a
 * format description.
 *
 * @author Guido R&ouml;&szlig;ling <a href="mailto:roessling@acm.org">
 * roessling@acm.org</a>
 * @version 1.1 2000-09-27
 */
public interface FormatSpecification
{
  /**
   * Return the default extension for this format type
   *
   * @return a string determining the format extension tag for this format.
   */
  public String getDefaultExtension();


  /**
   * Return a short description of this format type
   *
   * @return a string describing this format.
   * @since version 1.1
   */
  public String getFormatDescription();


  /**
   * Return the MIME type for this format type
   *
   * @return the MIME type of this format.
   * @since version 1.1
   */
  public String getMIMEType();
}
