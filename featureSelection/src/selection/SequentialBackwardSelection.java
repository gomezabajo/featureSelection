package selection;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import selection.utils.FeatureSelectionUtils;

/**
 * Performs Sequential Backward Selection (SBS)
 * <p>
 * - Starts with full set of features
 * - Repeatedly removes the "worst" feature until
 * stopping criteria is met,
 * 
 * Adapted from https://github.com/benjaminpatrickevans/FeatureSelectionWithJava
 */
public class SequentialBackwardSelection extends FeatureSelection {
	
	public SequentialBackwardSelection(Set<Integer> features, Map<Integer, Double> im, Map<Integer, Map<Integer, Double>> imx) {
		super(features, im, imx);
	}

	@Override
    public Set<Integer> select(int maxNumFeatures, boolean print) {
        // To begin with all features are selected
        Set<Integer> selectedFeatures = getAllFeatureIndices();

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
        double iterationsWithoutImprovement = 0;

        while (iterationsWithoutImprovement == 0) {
            int feature = worst(selectedFeatures);

            // No more valid features
            if (feature == -1) break;

            // Remove the feature so we do not keep selecting the same one
            selectedFeatures.remove(feature);

            accuracy = evaluate(selectedFeatures);

            // If this is the highest so far, and also valid (i.e < number of features required)
            if ((greaterThan(accuracy, highestAccuracy) || (equalTo(accuracy, highestAccuracy) && selectedFeatures.size() < bestSoFar.size()))
                    && selectedFeatures.size() <= maxNumFeatures) {
                highestAccuracy = accuracy;
                // Make a copy, so we don't accidentally modify this
                bestSoFar = new LinkedHashSet<>(selectedFeatures);
            }

            if (lessThanOrEqualTo(accuracy, lastAccuracy)) {
                iterationsWithoutImprovement++;
            } else {
                iterationsWithoutImprovement = 0;
            }

            lastAccuracy = accuracy;

            printAccuracy(selectedFeatures.size(), accuracy);
            if (print) {
            	System.out.println("; " + FeatureSelectionUtils.featureSet2String(selectedFeatures));
            }
        }

        return bestSoFar;
    }
}