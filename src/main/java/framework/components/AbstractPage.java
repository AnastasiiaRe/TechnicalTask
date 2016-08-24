package framework.components;


import framework.platform.web.PageLoader;

/**
 * Abstract class which defines default initialization state for page objects.
 */
public abstract class AbstractPage implements PageLoader {
    // Initialization state of WebPage
    /** The page initialized. */
    protected boolean pageInitialized;
    // used to determine our locale (e.g. US, UK, DE, etc.)
    /** The site. */
    protected String site;
    /** The page title. */
    protected String pageUrl;

    /** The UNKNOWN_PAGE_TITLE. */
    private static final String UNKNOWN_PAGE_TITLE = "unknown-url";

    /** Constructor. */
    protected AbstractPage() {
        pageUrl = UNKNOWN_PAGE_TITLE;
        pageInitialized = false;
    }

    /** Will return page initialization state. */
    public boolean isInitialized() {
        return pageInitialized;
    }


}
