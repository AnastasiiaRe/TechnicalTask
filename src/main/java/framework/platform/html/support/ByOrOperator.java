package framework.platform.html.support;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends {@link By} and provides a mechanism for locating an element from a list of {@link By}s where the element
 * found will be the first match in the list.
 */

public class ByOrOperator extends By {
    private List<By> bys;

    /** Constructor. */
    public ByOrOperator(List<By> bys) {
        this.bys = bys;
    }

    /** Overload of the findElement method. Will return first element which have been found on page. */
    @Override
    public WebElement findElement(SearchContext context) {
        List<WebElement> elements = findElements(context);
        if (elements.size() == 0) {
            throw new NoSuchElementException("Cannot locate an element using " + toString());
        }
        return elements.get(0);
    }

    /** Overload of the findElements method. Will return all elements which have been found on page. */
    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> result;
        for (By by : bys) {
            try {
                result = by.findElements(context);
                if (result != null && result.size() != 0) {
                    return result;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    /** Overload of toString method. Will return string value of all {@link By} */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("By.OrOperator(");
        stringBuilder.append("{");

        boolean first = true;
        for (By by : bys) {
            stringBuilder.append((first ? "" : ",")).append(by);
            first = false;
        }
        stringBuilder.append("})");
        return stringBuilder.toString();
    }
}
