package test.cs.teamproject.sabrewulf.map;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.map.Divider;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DividerTest {

    @Test
    public void InitialisationTest() {
        Divider div = new Divider(true);
        assertEquals(true, div.isActive(), "isActive function failed");
        assertEquals(Divider.DividerType.WALL, div.getDividerType(), "Divider type is wrong");

        Divider div1 = new Divider(false);
        assertEquals(false, div1.isActive(), "isActive function failed");
        assertEquals(Divider.DividerType.EMPTY, div1.getDividerType(), "Divider type is wrong");
    }

    @Test
    public void ActivateAndInactivateTest() {
        Divider div1 = new Divider(true);
        assertEquals(true, div1.isActive(), "isActive function failed");
        assertEquals(Divider.DividerType.WALL, div1.getDividerType(), "Divider type is wrong");
        div1.inactivate();
        assertEquals(false, div1.isActive(), "isActive function failed after inactivate function used");
        assertEquals(Divider.DividerType.EMPTY, div1.getDividerType(), "Divider type is wrong after inactivate function used");
        div1.activate();
        assertEquals(true, div1.isActive(), "isActive function failed after activate function used");
        assertEquals(Divider.DividerType.WALL, div1.getDividerType(), "Divider type is wrong after activate function used");

        Divider div2 = new Divider(false);
        assertEquals(false, div2.isActive(), "isActive function failed");
        assertEquals(Divider.DividerType.EMPTY, div2.getDividerType(), "Divider type is wrong");
        div2.activate();
        assertEquals(true, div2.isActive(), "isActive function failed after activate function used");
        assertEquals(Divider.DividerType.WALL, div2.getDividerType(), "Divider type is wrong after activate function used");
        div2.inactivate();
        assertEquals(false, div2.isActive(), "isActive function failed after inactivate function used");
        assertEquals(Divider.DividerType.EMPTY, div2.getDividerType(), "Divider type wrong after inactivate function used");
    }

    @Test
    public void SetColoursTest() {
        Divider div = new Divider(true);

        assertEquals(Divider.DividerType.WALL, div.getDividerType(), "Initial divider type wrong");
        assertEquals(false, div.isColoured(), "isColoured function failed");

        div.setBlue(true);
        assertEquals(true, div.isColoured(), "isColoured function failed");
        assertEquals(true, div.isBlue(), "isBlue function failed");
        assertEquals(Divider.DividerType.BLUEDOOR, div.getDividerType(), "Divider type is wrong after setBlue function used");

        div.setGreen(true);
        assertEquals(true, div.isColoured(), "isColoured function failed");
        assertEquals(true, div.isGreen(), "isGreen function failed");
        assertEquals(Divider.DividerType.GREENDOOR, div.getDividerType(), "Divider type wrong after setGreen function used");

        div.setYellow(true);
        assertEquals(true, div.isColoured(), "isColoured function failed");
        assertEquals(true, div.isYellow(), "isYellow function failed");
        assertEquals(Divider.DividerType.YELLOWDOOR, div.getDividerType(), "Divider type is wrong after setYellow function used");
    }

}
