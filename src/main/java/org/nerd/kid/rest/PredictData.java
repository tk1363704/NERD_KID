package org.nerd.kid.rest;

import org.nerd.kid.arff.AccessArff;
import smile.classification.RandomForest;
import smile.data.AttributeDataset;
import smile.data.parser.ArffParser;

import java.io.*;

public class PredictData {
    //classification model of Random Forest
    private RandomForest forest = null;
    private ArffParser arffParser = new ArffParser();
    private AttributeDataset attributeDataset = null;
    AccessArff accessArff = new AccessArff();

    public int[] predictTestData(double[][] Testx) {
        int[] yPredict = new int[Testx.length];
        // predicting the test
        for (int i = 0; i < Testx.length; i++) {
            yPredict[i] = forest.predict(Testx[i]);
        }
        return yPredict;
    }

    public String[] predictNewTestData(double[][] testX) throws Exception{
        String file = "data/Training.arff";
        // parsing the initial file to get the response index
        attributeDataset = arffParser.parse(new FileInputStream(file));

        // getting the response index
        int responseIndex = attributeDataset.attributes().length - 1;

        // setResponseIndex is response variable; for classification, it is the class label; for regression, it is of real value
        arffParser = new ArffParser().setResponseIndex(responseIndex);

        // parsing the file to get the dataset
        attributeDataset = arffParser.parse(file);

        double[][] datax = attributeDataset.toArray(new double[attributeDataset.size()][]);
        int[] datay = attributeDataset.toArray(new int[attributeDataset.size()]);

        // training with Random Forest classification
        forest = new RandomForest(attributeDataset.attributes(), datax, datay, 100);
        int[] resultPrediction = new int[testX.length];

        // getting the index label of the class
        String[] nameClass = accessArff.readClassArff(new File(file));
        for (int i = 0; i < testX.length; i++) {
            resultPrediction[i] = forest.predict(testX[i]);
        }
        String[] result = new String[testX.length];
        for (int i = 0; i < resultPrediction.length; i++) {
            int idx = resultPrediction[i];
            result[i] = nameClass[idx];
        }

        return result;
    }

}

