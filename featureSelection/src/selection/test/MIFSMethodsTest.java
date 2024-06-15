package selection.test;

import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import selection.FeatureSelection;
import selection.SequentialBackwardSelection;
import selection.SequentialForwardSelection;
import selection.utils.FeatureSelectionUtils;

/**
 * The main class for running feature selection
 * from command line. Also shows how this should
 * be used from java code.
 */
public class MIFSMethodsTest {

	private static int[] variables = {0, 1, 2, 3, 4};
	
	private static double[] imValues = { 0.4, 0.25, 0.3, 0.2, 0.35 };
	private static double[] imxValues = { 0.3, 0.2, 0.1, 0.2, -1, 0.2, 0.25, 0.15, -1, 0.15, 0.2, -1, 0.1};

	private static Map<Integer, Double> im = new LinkedHashMap<Integer, Double>();
	private static Map<Integer, Map<Integer, Double>> imx = new LinkedHashMap<Integer, Map<Integer, Double>>();
	
	private static void process() {
		
		int i = 0;
		for (double imV : imValues) {
			im.put(i, imV);
			i++;
		}
		
		int k = 0;
		int m = 0;
		for (i = 0; i < imxValues.length; i++) {
			k++;
			for (int j = k; j < variables.length + 1 && i < imxValues.length; j++) {
				m++;
				if (m != k) {
					if (imx.get(k - 1) == null) {
						imx.put(k - 1, new LinkedHashMap<Integer, Double>());
					}
					imx.get(k - 1).put(m - 1, imxValues[i]);
					i++;
				}
			}
			m = k;
		}
	}

	public static void main(String[] args) {
		
		process();

		/*
		for (int v : im.keySet()) {
			System.out.println("x" + (v + 1) + " - " + im.get(v));
		}
		System.out.println("----");
		for (int v1 : imx.keySet()) {
			for (int v2 : imx.get(v1).keySet()) {
				System.out.println("x" + (v1 + 1) + ", x" + (v2 + 1) + " - " + imx.get(v1).get(v2));
			}
		}
		*/
		
		Set<Integer> features = new LinkedHashSet<>();
		for (int v : variables) {
			features.add(v);
		}
		FeatureSelection backward = new SequentialBackwardSelection(features, im, imx);
		System.out.println("backward:--------------------");
		Set<Integer> backwardSelected = backward.select(features.size(), true);
		System.out.println("selected: " + FeatureSelectionUtils.featureSet2String(backwardSelected));
		System.out.println("-----------------------------");
		System.out.println("forward:---------------------");
		FeatureSelection forward = new SequentialForwardSelection(features, im, imx);
		Set<Integer> forwardSelected = forward.select(true);
		System.out.println("selected: " + FeatureSelectionUtils.featureSet2String(forwardSelected));
		System.out.println("-----------------------------");
	}
}
