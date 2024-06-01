package it.polimi.ingsw.server.model.card.corner;

import it.polimi.ingsw.server.model.card.ResourceCard;
import it.polimi.ingsw.server.model.card.corner.Corner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CornerTest {

    final int numOfResourceCards = 10;

    @Test
    @DisplayName("Test Corner.setVisible() method")
    public void testSetVisibleMethod() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/assets/resourcecards/resourceCard2.json"))));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonData = stringBuilder.toString();
            ResourceCard currCard = new ResourceCard(jsonData);
            List<Corner> currCorners = currCard.getCorners();

            boolean prevVisibility = currCorners.get(2).isVisible();
            currCorners.get(2).setVisible(!prevVisibility);

            assertEquals(!prevVisibility, currCorners.get(2).isVisible());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

   /*@Test
    @DisplayName("Validate that corners in a single resource card have 4x2 different CornerLocationEnum")
    public void validateAllCornersLocation() {

        File currFile = Paths.get("src/main/resources/assets/resourcecards/resourceCard2.json").toFile();
        ResourceCard currCard = new ResourceCard(currFile);
        List<Corner> currCorners = currCard.getCorners();

        //CornerLocationEnum counters
        int tlCount = 0;
        int trCount = 0;
        int brCount = 0;
        int blCount = 0;

        for (Corner corner : currCorners) {

            if (corner.getLocation() == CornerLocationEnum.TOP_LEFT) tlCount++;
            if (corner.getLocation() == CornerLocationEnum.TOP_RIGHT) trCount++;
            if (corner.getLocation() == CornerLocationEnum.BOTTOM_RIGHT) brCount++;
            if (corner.getLocation() == CornerLocationEnum.BOTTOM_LEFT) blCount++;

        }
        if (!(trCount == 2 && tlCount == 2 && brCount == 2 && blCount == 2))
            fail("CornerLocationEnum counters are not equals to 2");
    }


    //TODO: add this test for all card type
    //Test to validate all ResourceCard's corners for every ResourceCard in 'resources/assets/resourcecards'
    @Test
    @DisplayName("Validate that all corners and their attributes in resourceCards are not null")
    public void validateNotNullAllCornersResourceCards() {

        // curr = current
        ResourceCard currCard;
        List<Corner> currCorners;
        File currFile;

        for (int i = 2; i <= numOfResourceCards; i++) {

            //Card selection
            currFile = Paths.get("src/main/resources/assets/resourcecards/resourceCard" + i + ".json").toFile();
            currCard = new ResourceCard(currFile);

            //Corners validation
            currCorners = currCard.getCorners();

            for (Corner corner : currCorners) {

                if (corner == null) {
                    fail("corner is null: card " + i);
                }

                //null attributes tests
                if (corner.getLocation() == null) {
                    fail("corner location is null: card " + i);

                }
                if (corner.getFace() == null) {
                    fail("corner face is null: card " + i);

                }
                if ((corner.getObject() == null) && (corner.getResource() == null)) {
                    fail("corner object/resource is null: card " + i);

                }
            }
        }
    }*/
}