package selection;

import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import selection.utils.FeatureSelectionUtils;

/**
 * Top level class for feature selection, holds the classifier
 * to use and has some helper functions such as selecting the
 * best and worst instances. This is an abstract class, so
 * should be extended to add a feature selection method -
 * it can not be instantiated.
 * 
 * Adapted from https://github.com/benjaminpatrickevans/FeatureSelectionWithJava
 */
public abstract class FeatureSelection {
	
	private Set<Integer> features;
	private Map<Integer, Double> im;
	private Map<Integer, Map<Integer, Double>> imx;
	
	public FeatureSelection(Set<Integer> features, Map<Integer, Double> im, Map<Integer, Map<Integer, Double>> imx) {
		this.features = new LinkedHashSet<>(features);
		this.im = new LinkedHashMap<Integer, Double>();
		for (int v : im.keySet()) {
			this.im.put(v, im.get(v));
		}
		this.imx = new LinkedHashMap<Integer, Map<Integer, Double>>();
		for (int v1 : imx.keySet()) {
			for (int v2 : imx.get(v1).keySet()) {
				if (this.imx.get(v1) == null) {
					this.imx.put(v1, new LinkedHashMap<Integer, Double>());
				}
				this.imx.get(v1).put(v2, imx.get(v1).get(v2));
			}
		}
		
	}

    /**
     * Returns the index of the "best" feature in the remaining set of features,
     * ie the feature which when added to the selectedFeatures maximises
     * the objective function.
     *
     * @param selectedFeatures
     * @param remainingFeatures
     * @return
     */
    protected int best(Set<Integer> selectedFeatures, Set<Integer> remainingFeatures) {
        double highest = -Integer.MAX_VALUE;
        int selected = -1;

        for (int feature : remainingFeatures) {
            Set<Integer> newFeatures = new LinkedHashSet<>(selectedFeatures);
            newFeatures.add(feature);

            double result = evaluate(newFeatures);
            if (result > highest) {
                highest = result;
                selected = feature;
            }
        }

        return selected;
    }

    /**
     * Finds and returns the index of the "worst" feature,
     * where worst is defined by the feature whose removal results in the
     * highest classification accuracy (i.e. an irrelevant or redundant feature)
     *
     * @param selectedFeatures
     * @return
     */
    protected int worst(Set<Integer> selectedFeatures) {
        double highestAccuracy = -Integer.MAX_VALUE;
        int selected = -1;

        for (int feature : selectedFeatures) {
            Set<Integer> newFeatures = new LinkedHashSet<>(selectedFeatures);
            newFeatures.remove(feature);

            double result = evaluate(newFeatures);
            if (result >= highestAccuracy) {
                highestAccuracy = result;
                selected = feature;
            }
        }

        return selected;
    }


    /**
     * Returns the classification accuracy on the validation
     * set using the specified features
     *
     * @param selectedFeatures
     * @return
     * @throws Exception
     */
    protected double evaluate(Set<Integer> selectedFeatures) {
    	double ret = 0.0;
    	for (int feature : selectedFeatures) {
    		ret = FeatureSelectionUtils.sumDouble(ret, im.get(feature));
    	}
    	int f1 = selectedFeatures.size() > 0 ? new ArrayList<>(selectedFeatures).get(0) : -1;
    	if (f1 == -1) {
    		return ret;
    	}
   		for (int f2 : selectedFeatures) {
   			if (f1 < f2) {
   				ret = FeatureSelectionUtils.sumDouble(ret, -imx.get(f1).get(f2));
   			}
    	}
    	return ret;
    }

    /**
     * Prints out the size and accuracy at each iteration of selection.
     * Only prints if DEBUG is set to true, does nothing otherwise.
     *
     * @param size
     * @param accuracy
     */
    protected void printAccuracy(int size, double accuracy) {
        System.out.print(size + ": " + accuracy);
    }

    /***
     * ===============
     * HELPER METHODS FOR DEALING WITH FLOATING POINT PRECISION EASILY
     * ===============
     */

    protected boolean greaterThan(double d1, double d2) {
        return Double.compare(d1, d2) > 0;
    }

    protected boolean lessThan(double d1, double d2) {
        return Double.compare(d1, d2) < 0;
    }

    protected boolean lessThanOrEqualTo(double d1, double d2) {
        return Double.compare(d1, d2) <= 0;
    }

    protected boolean equalTo(double d1, double d2) {
        return Double.compare(d1, d2) == 0;
    }
    
    protected Set<Integer> getAllFeatureIndices() {
    	return this.features;
    }
    
    public Set<Integer> select(boolean print) {
    	return new LinkedHashSet<>();
    }

    public Set<Integer> select(int maxNumFeatures, boolean print) {
    	return new LinkedHashSet<>();
    }
}
