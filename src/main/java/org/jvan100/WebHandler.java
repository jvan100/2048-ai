package org.jvan100;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebHandler {

    private final String GAME_URL = "https://play2048.co/";

    private final WebDriver driver;

    private WebElement oldTile;

    WebHandler() {
        System.setProperty("webdriver.chrome.driver", "target/classes/chromedriver.exe");

        this.driver = new ChromeDriver();
        this.oldTile = null;

        driver.get(GAME_URL);
    }

    void close() {
        driver.quit();
    }

    int[][] readStartBoard() {
        int[][] board = new int[4][4];

        new WebDriverWait(driver, 3)
                .until(webDriver -> webDriver.findElements(By.className("tile-new")))
                .forEach(tile -> addTileToBoard(board, tile));

        return board;
    }

    void refreshBoard(int[][] board) {
        final WebElement newTile = new WebDriverWait(driver, 3)
                .until(ExpectedConditions.refreshed(webDriver -> {
                    final WebElement tile = webDriver.findElement(By.className("tile-new"));
                    return (tile.equals(oldTile)) ? null : tile;
                }));

        oldTile = newTile;

    }

    private void addTileToBoard(int[][] board, WebElement tile) {
        final String[] classes = tile.getAttribute("class").split(" ");

        final int val = log(Integer.parseInt(classes[1].substring(5)), 2);

        final int row = classes[2].charAt(16) - '1';
        final int col = classes[2].charAt(14) - '1';

        board[row][col] = val;
    }

    void move(Keys key) {
        driver.findElement(By.tagName("body")).sendKeys(key);
    }

    boolean checkGameOver() {
        try {
            driver.findElement(By.className("game-over"));
        } catch (NoSuchElementException e) {
            return false;
        }

        return true;
    }

    private int log(int x, int base) {
        return (int) (Math.log(x) / Math.log(base));
    }

}
