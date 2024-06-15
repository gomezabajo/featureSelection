package selection;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import selection.utils.FeatureSelectionUtils;

/**
 * Performs Sequential Floating Forward Selection (SFFS)
 * <p>
 * - Starts with empty set of features
 * - Adds the "best" feature until stopping criteria is met
 * 
 * Adapted from https://github.com/benjaminpatrickevans/FeatureSelectionWithJava
 */
public class SequentialForwardSelection extends FeatureSelection {
	
	public SequentialForwardSelection(Set<Integer> features, Map<Integer, Double> im, Map<Integer, Map<Integer, Double>> imx) {
		super(features, im, imx);
	}

	@Override
    public Set<Integer> select(boolean print) {
        // To begin with no features are selected, so all the indices from 0..totalFeatures are remaining
        Set<Integer> remainingFeatures = getAllFeatureIndices();

        // Subset of only selected features indices
        Set<Integer> selectedFeatures = new LinkedHashSet<>();

        // Keep track of the best solution, so we never get worse
        double highestAccuracy = 0;
        Set<Integer> bestSoFar = new LinkedHashSet<>();
        double accuracy = evaluate(selectedFeatures);
        double lastAccuracy = accuracy;

        printAccuracy(selectedFeatures.size(), accuracy);
        if (print) {
        	System.out.println("; " + FeatureSelectionUtils.featureSet2String(selectedFeatures));
        }

        // Number of iterations with no improvement
        double noImprovement = 0;

        while (noImprovement == 0) {
            int feature = best(selectedFeatures, remainingFeatures);
            // No more valid features
            if (feature == -1) break;

            selectedFeatures.add(feature);
            // Remove the feature so we do not keep selecting the same one
            remainingFeatures.remove(feature);

            accuracy = evaluate(selectedFeatures);

            if (greaterThan(accuracy, highestAccuracy)) {
                highestAccuracy = accuracy;
                // Make a copy, so we don't accidentally modify the best subset
                bestSoFar = new LinkedHashSet<>(selectedFeatures);
            }

            printAccuracy(selectedFeatures.size(), accuracy);
            if (print) {
            	System.out.println("; " + FeatureSelectionUtils.featureSet2String(selectedFeatures));
            }

            if (lessThanOrEqualTo(accuracy, lastAccuracy)) {
                noImprovement++;
            } else {
                noImprovement = 0;
            }
            lastAccuracy = accuracy;
        }

        return bestSoFar;
    }
}