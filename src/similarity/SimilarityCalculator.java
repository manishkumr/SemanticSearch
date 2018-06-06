package similarity;

public class SimilarityCalculator {
	
	
	public double getCosineSimilarity(double[] v1, double[] v2) {
		
		double similarity = 0;
		if (v1.length != v2.length) {
            System.out.println("Cosine : Both vector sizes are not same");
            return 0.0;
        } else {
//            System.out.println("dot : " + calDotVectors(v1, v2));
//            System.out.println("v1 size : " + calVectorSize(v1));
//            System.out.println("v2 size : " + calVectorSize(v2));
        	double sizeProduct = calVectorSize(v1) * calVectorSize(v2);
        	if(sizeProduct==0){
        		return 0.0;
        	}
            similarity = calDotVectors(v1, v2) /sizeProduct ;
        }
		
		return similarity;
		
	}
	
	private static double calDotVectors(double[] v1, double[] v2) {
        double dotVal = 0;
        if (v1.length != v2.length) {
            System.out.println("DotVal : Both vector sizes are not same");
            System.exit(0);
        } else {
            for (int i = 0; i < v1.length; i++) {
                dotVal += (v1[i] * v2[i]);

            }
        }

        return dotVal;
    }
	
	private static double calVectorSize(double[] v1) {
        double size = 0;
        double sum = 0;
        for (double i : v1) {
            sum += (i * i);
        }
        size = Math.sqrt(sum);
        return size;
    }

}
