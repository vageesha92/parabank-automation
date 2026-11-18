package io.github.vageesha92.ui;

import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.ExtrasPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ExtrasDragTest extends BaseUiTest {

    @Test
    public void payeesReorderByDrag() {
        ExtrasPage extras = new ExtrasPage(DriverManager.getDriver()).open();

        assertEquals(extras.dragPayeeBelow("Electric Co", "City Rent"),
                "Order: Water Board, City Rent, Electric Co, Phone Plus");
    }
}
