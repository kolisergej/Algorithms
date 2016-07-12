import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture mPicture;
    private double[][] mEnergyMatrix;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException();
        }
        mPicture = new Picture(picture);
        
        int width = mPicture.width();
        int height = mPicture.height();
        
        mEnergyMatrix = new double[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mEnergyMatrix[j][i] = myEnergy(j, i);
            }
        }
    }

    public Picture picture() {
        return new Picture(mPicture);
    }

    public int width() {
        return mPicture.width();
    }

    public int height() {
        return mPicture.height();
    }

    public double energy(int column, int row) {
        if (column < 0 || column > width() - 1 || row < 0 || row > height() - 1) {
            throw new IndexOutOfBoundsException();
        }
        return mEnergyMatrix[column][row];
    }
    
    private double myEnergy(int column, int row) {
        double result = 1000;
        if (column != 0 && row != 0 && column != width() - 1 && row != height() - 1) {            
            Color colorLeft = mPicture.get(column-1, row);
            Color colorRight = mPicture.get(column+1, row);
            int redX = colorLeft.getRed() - colorRight.getRed();
            int greenX = colorLeft.getGreen() - colorRight.getGreen();
            int blueX = colorLeft.getBlue() - colorRight.getBlue();
            double gradientXSquared = redX * redX + greenX * greenX + blueX * blueX;
            
            Color colorHigher = mPicture.get(column, row-1);
            Color colorLower = mPicture.get(column, row+1);
            int redY = colorLower.getRed() - colorHigher.getRed();
            int greenY = colorLower.getGreen() - colorHigher.getGreen();
            int blueY = colorLower.getBlue() - colorHigher.getBlue();
            
            double gradientYSquared = redY * redY + greenY * greenY + blueY * blueY;
            result = Math.sqrt(gradientXSquared + gradientYSquared);
        }
        return result;
    }
    
    public int[] findVerticalSeam() {
        int width = mEnergyMatrix.length;
        int height = mEnergyMatrix[0].length;
        
        double[][] minEnergies = new double[width][height];
        for (int column = 0; column < width; column++) {
            minEnergies[column][height - 1] = mEnergyMatrix[column][height - 1];
        }
        for (int row = height - 2; row >= 0; row--) {
            for (int column = 0; column < width; column++) {
                double minInThreeElementsRowLow = minEnergies[column][row + 1];
                if (column > 0 && minInThreeElementsRowLow > minEnergies[column - 1][row + 1]) {
                    minInThreeElementsRowLow = minEnergies[column - 1][row + 1];
                }
                if (column < width - 1 && minInThreeElementsRowLow > minEnergies[column + 1][row + 1]) {
                    minInThreeElementsRowLow = minEnergies[column + 1][row + 1];
                }
                minEnergies[column][row] = minInThreeElementsRowLow + mEnergyMatrix[column][row];
            }
        } 

        int[] verticalSeam = new int[height];
        verticalSeam[0] = 0;

        for (int column = 1; column < width; column++) {
            int minValueColumnIndex = verticalSeam[0];
            if (minEnergies[column][0] < minEnergies[minValueColumnIndex][0]) {
                verticalSeam[0] = column;
            }
        }
        
        for (int row = 1; row < height; row++) {
            int previousColumnIndex = verticalSeam[row - 1];
            verticalSeam[row] = previousColumnIndex;
            if (previousColumnIndex != 0 && minEnergies[verticalSeam[row]][row] > minEnergies[previousColumnIndex - 1][row]) {
                verticalSeam[row] = previousColumnIndex - 1;
            }
            if (previousColumnIndex < width - 1 && minEnergies[verticalSeam[row]][row] > minEnergies[previousColumnIndex + 1][row]) {
                verticalSeam[row] = previousColumnIndex + 1;
            }
        }
        
        return verticalSeam;
    }

    public int[] findHorizontalSeam() {     
        int width = mEnergyMatrix.length;
        int height = mEnergyMatrix[0].length;
        
        double[][] tmpEnergyMatrix = new double[width][height];
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                tmpEnergyMatrix[column][row] = mEnergyMatrix[column][row];
            }
        }
            
        mEnergyMatrix = new double[height][width];
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                mEnergyMatrix[row][column] = tmpEnergyMatrix[column][row];
            }
        }
        
        int[] horizontalSeam = findVerticalSeam();
        
        mEnergyMatrix = new double[width][height];
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                mEnergyMatrix[column][row] = tmpEnergyMatrix[column][row];
            }
        }

        return horizontalSeam;
    }
    
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width() - 1) {
                throw new IllegalArgumentException();
            }
            if (i > 0 && Math.abs(seam[i] - seam[i-1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (width() <= 1) {
            throw new IllegalArgumentException();
        }
        
        int height = mPicture.height();
        int width = mPicture.width();
        Picture tmpPicture = new Picture(mPicture);
        
        mPicture = new Picture(width-1, height);
        
        double[][] tmpEnergy = new double[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tmpEnergy[j][i] = mEnergyMatrix[j][i];
            }
        }
        
        for (int i = 0; i < height; i++) {
            boolean shift = false;
            for (int j = 0; j < width-1; j++) {
                if (j == seam[i]) {
                    shift = true;
                } 
                if (shift) {
                    mPicture.set(j, i, tmpPicture.get(j+1, i));
                } else {
                    mPicture.set(j, i, tmpPicture.get(j, i));
                }
            }
        }
        
        mEnergyMatrix = new double[width-1][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width-1; j++) {
                if (j == seam[i]-1 || j == seam[i] || j == seam[i]+1) {
                    mEnergyMatrix[j][i] = myEnergy(j, i);
                } else if (j > seam[i]+1) {
                    mEnergyMatrix[j][i] = tmpEnergy[j+1][i]; 
                } else {
                    mEnergyMatrix[j][i] = tmpEnergy[j][i];
                }                
            }
        }
    }
    

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1) {
                throw new IllegalArgumentException();
            }
            if (i > 0 && Math.abs(seam[i] - seam[i-1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (height() <= 1) {
            throw new IllegalArgumentException();
        }
        
        int height = mPicture.height();
        int width = mPicture.width();
        Picture tmpPicture = new Picture(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tmpPicture.set(i, j, mPicture.get(j, i));
            }
        }
        mPicture = new Picture(width, height-1);
        double[][] tmpEnergy = new double[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tmpEnergy[j][i] = mEnergyMatrix[j][i];
            }
        }
             
        for (int i = 0; i < width; i++) {
            boolean shift = false;
            for (int j = 0; j < height-1; j++) {
                if (j == seam[i]) {
                    shift = true;
                }                 
                if (shift) {
                    mPicture.set(i, j, tmpPicture.get(j+1, i));
                } else {
                    mPicture.set(i, j, tmpPicture.get(j, i));
                }
            }
        }
        mEnergyMatrix = new double[width][height-1];
        
        for (int i = 0; i < height-1; i++) {
            for (int j = 0; j < width; j++) {
                if (i == seam[j]-1 || i == seam[j] || i == seam[j]+1) {
                    mEnergyMatrix[j][i] = myEnergy(j, i);
                } else if (i > seam[j]+1) {
                    mEnergyMatrix[j][i] = tmpEnergy[j][i+1];
                } else {
                    mEnergyMatrix[j][i] = tmpEnergy[j][i];
                }
            }
        }
    }
}
