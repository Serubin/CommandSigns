package com.hans.CommandSigns;

public class CommandSignsText {

    private String[] text = null;

    /**
     * Initializes and sets text to null until set.
     * 
     * @param index
     *            Length of signs
     * 
     */
    public CommandSignsText(int index) {
        text = new String[index];
        for (int i = 0; i < index; i++) {
            this.text[i] = null;
        }
    }

    /**
     * Initializes and sets text from array
     * 
     * @param lines
     *            array of text
     */
    public CommandSignsText(String[] lines) {
        text = new String[lines.length];
        this.text = lines;
    }

    /**
     * Initializes and sets text from lines. remaining items are set to null
     * 
     * @param index
     *            Max amount of lines
     * @param lines
     *            Lines
     */
    public CommandSignsText(int index, String[] lines) {
        if (index < lines.length) {
            index = lines.length;
        }
        this.text = new String[index];
        for (int i = 0; i < index; i++) {
            if (lines[i] != null) {
                this.text[i] = lines[i];
            } else {
                this.text[i] = null;
            }
        }
    }

    /**
     * Get text on sign
     * 
     * @return text
     */
    public String[] getText() {
        return this.text;
    }

    @Override
    /**
     *  Prints data to string
     *  @return text
     */
    public String toString() {
        String string = "";
        String line;
        for (int i = 0; i < 10; i++) {
            line = this.getLine(i);
            if (line != null) {
                string = string.concat(this.getLine(i) + ((i != 9) ? " " : ""));
            }
        }
        return string;
    }

    /**
     * Gets line of text from index
     * 
     * @param index
     *            line to be returned
     * @return line of text
     */
    public String getLine(int index) {
        if (index < 0 || index >= this.text.length) {
            return null;
        }
        return this.text[index];
    }

    /**
     * Sets a specific line's text
     * 
     * @param index
     *            The line to be changed
     * @param line
     *            The line of text
     * @return boolean
     */
    public boolean setLine(int index, String line) {
        if (index < 0 || index >= 10) {
            return false;
        }
        this.text[index] = line;
        return true;
    }

    /**
     * Sets the entire text array
     * 
     * @param lines
     *            Array of text to be set
     * @return boolean
     */
    public boolean setLine(String[] lines) {
        this.text = lines;
        return true;
    }

}
