package edu.pitt.rods.apollo.statetransitionnetwork.braithwaite;


public class Parameters {

	/***
	 * recovered these parameters describe the transition to recovered. We
	 * really don't care about these so much from a cost standpoint, but if wef
	 * ever want to accurately plot the transitions over time, we need to
	 * revisit these.
	 */
	private static final Double ASX_TO_RECOVERED_MU = 5.780744;
	private static final Double ASX_TO_RECOVERED_SIGMA = 0.2;

	private static final Double PROD_TO_RECOVERED_MU = 6.059;
	private static final Double PROD_TO_RECOVERED_SIGMA = 0.2;

	private static final Double FUL_TO_RECOVERED_MU = 6.059;
	private static final Double FUL_TO_RECOVERED_SIGMA = 0.2;
	/*** end recovered ***/

	private static final Double ASX_TO_PROD_MU = 4.548033;
	private static final Double ASX_TO_PROD_SIGMA = 0.19236;

	private static final Double PROD_TO_FUL_MU = 4.58444;
	private static final Double PROD_TO_FUL_SIGMA = 0.442275;

	/* using Buckridge parameters */
	private static final Double FUL_TO_DEAD_MU = 3.583;
	private static final Double FUL_TO_DEAD_SIGMA = 0.364643;

	private static final Double PO_EFFICACY = 0.9;
	// needed to overcome rounding error?!
	private static final Double ONE_MINUS_PO_EFFICACY = 0.1;

	private static final Double FUL_TREATMENT_EFFICACY = 0.55;
	private static final Double ONE_MINUS_FUL_TREATMENT_EFFICACY = 0.45;

	private static final Double PROD_TREATMENT_EFFICACY = 0.55;
	private static final Double ONE_MINUS_PROD_TREATMENT_EFFICACY = 0.45;

	private static final String FUL_TO_DEAD_MU_NAME = "muFulminantToDead";
	private static final String FUL_TO_DEAD_SIGMA_NAME = "sigmaFulminantToDead";
	private static final String PROD_TO_FUL_MU_NAME = "muProdromalToFulminant";
	private static final String PROD_TO_FUL_SIGMA_NAME = "sigmaProdromalToFulminant";
	private static final String ASX_TO_PROD_MU_NAME = "muAsymptomaticToProdromal";
	private static final String ASX_TO_PROD_SIGMA_NAME = "sigmaAsymptomaticToProdromal";

	protected static Double asxToRecoveredMu = ASX_TO_RECOVERED_MU;
	protected static Double asxToRecoveredSigma = ASX_TO_RECOVERED_SIGMA;
	protected static Double prodToRecoveredMu = PROD_TO_RECOVERED_MU;
	protected static Double prodToRecoveredSigma = PROD_TO_RECOVERED_SIGMA;
	protected static Double fulToRecoveredMu = FUL_TO_RECOVERED_MU;
	protected static Double fulToRecoveredSigma = FUL_TO_RECOVERED_SIGMA;
	protected static Double asxToProdMu = ASX_TO_PROD_MU;
	protected static Double asxToProdSigma = ASX_TO_PROD_SIGMA;
	protected static Double prodToFulMu = PROD_TO_FUL_MU;
	protected static Double prodToFulSigma = PROD_TO_FUL_SIGMA;
	protected static Double fulToDeadMu = FUL_TO_DEAD_MU;
	protected static Double fulToDeadSigma = FUL_TO_DEAD_SIGMA;
	protected static Double poEfficacy = PO_EFFICACY;
	protected static Double oneMinusPoEfficacy = ONE_MINUS_PO_EFFICACY;
	protected static Double fulTreatmentEfficacy = FUL_TREATMENT_EFFICACY;
	protected static Double oneMinusFulTreatmentEfficacy = ONE_MINUS_FUL_TREATMENT_EFFICACY;
	protected static Double prodTreatmentEfficacy = PROD_TREATMENT_EFFICACY;
	protected static Double oneMinusProdTreatmentEfficacy = ONE_MINUS_PROD_TREATMENT_EFFICACY;
	protected static String fulToDeadMuName = FUL_TO_DEAD_MU_NAME;
	protected static String fulToDeadSigmaName = FUL_TO_DEAD_SIGMA_NAME;
	protected static String prodToFulMuName = PROD_TO_FUL_MU_NAME;
	protected static String prodToFulSigmaName = PROD_TO_FUL_SIGMA_NAME;
	protected static String asxToProdMuName = ASX_TO_PROD_MU_NAME;
	protected static String asxToProdSigmaName = ASX_TO_PROD_SIGMA_NAME;

	public static Double getAsxToRecoveredMu() {
		return asxToRecoveredMu;
	}

	public static void setAsxToRecoveredMu(Double asxToRecoveredMu) {
		Parameters.asxToRecoveredMu = asxToRecoveredMu;
	}

	public static Double getAsxToRecoveredSigma() {
		return asxToRecoveredSigma;
	}

	public static void setAsxToRecoveredSigma(Double asxToRecoveredSigma) {
		Parameters.asxToRecoveredSigma = asxToRecoveredSigma;
	}

	public static Double getProdToRecoveredMu() {
		return prodToRecoveredMu;
	}

	public static void setProdToRecoveredMu(Double prodToRecoveredMu) {
		Parameters.prodToRecoveredMu = prodToRecoveredMu;
	}

	public static Double getProdToRecoveredSigma() {
		return prodToRecoveredSigma;
	}

	public static void setProdToRecoveredSigma(Double prodToRecoveredSigma) {
		Parameters.prodToRecoveredSigma = prodToRecoveredSigma;
	}

	public static Double getFulToRecoveredMu() {
		return fulToRecoveredMu;
	}

	public static void setFulToRecoveredMu(Double fulToRecoveredMu) {
		Parameters.fulToRecoveredMu = fulToRecoveredMu;
	}

	public static Double getFulToRecoveredSigma() {
		return fulToRecoveredSigma;
	}

	public static void setFulToRecoveredSigma(Double fulToRecoveredSigma) {
		Parameters.fulToRecoveredSigma = fulToRecoveredSigma;
	}

	public static Double getAsxToProdMu() {
		return asxToProdMu;
	}

	public static void setAsxToProdMu(Double asxToProdMu) {
		Parameters.asxToProdMu = asxToProdMu;
	}

	public static Double getAsxToProdSigma() {
		return asxToProdSigma;
	}

	public static void setAsxToProdSigma(Double asxToProdSigma) {
		Parameters.asxToProdSigma = asxToProdSigma;
	}

	public static Double getProdToFulMu() {
		return prodToFulMu;
	}

	public static void setProdToFulMu(Double prodToFulMu) {
		Parameters.prodToFulMu = prodToFulMu;
	}

	public static Double getProdToFulSigma() {
		return prodToFulSigma;
	}

	public static void setProdToFulSigma(Double prodToFulSigma) {
		Parameters.prodToFulSigma = prodToFulSigma;
	}

	public static Double getFulToDeadMu() {
		return fulToDeadMu;
	}

	public static void setFulToDeadMu(Double fulToDeadMu) {
		Parameters.fulToDeadMu = fulToDeadMu;
	}

	public static Double getFulToDeadSigma() {
		return fulToDeadSigma;
	}

	public static void setFulToDeadSigma(Double fulToDeadSigma) {
		Parameters.fulToDeadSigma = fulToDeadSigma;
	}

	public static Double getPoEfficacy() {
		return poEfficacy;
	}

	public static void setPoEfficacy(Double poEfficacy) {
		Parameters.poEfficacy = poEfficacy;
	}

	public static Double getOneMinusPoEfficacy() {
		return oneMinusPoEfficacy;
	}

	public static void setOneMinusPoEfficacy(Double oneMinusPoEfficacy) {
		Parameters.oneMinusPoEfficacy = oneMinusPoEfficacy;
	}

	public static Double getFulTreatmentEfficacy() {
		return fulTreatmentEfficacy;
	}

	public static void setFulTreatmentEfficacy(Double fulTreatmentEfficacy) {
		Parameters.fulTreatmentEfficacy = fulTreatmentEfficacy;
	}

	public static Double getOneMinusFulTreatmentEfficacy() {
		return oneMinusFulTreatmentEfficacy;
	}

	public static void setOneMinusFulTreatmentEfficacy(
			Double oneMinusFulTreatmentEfficacy) {
		Parameters.oneMinusFulTreatmentEfficacy = oneMinusFulTreatmentEfficacy;
	}

	public static Double getProdTreatmentEfficacy() {
		return prodTreatmentEfficacy;
	}

	public static void setProdTreatmentEfficacy(Double prodTreatmentEfficacy) {
		Parameters.prodTreatmentEfficacy = prodTreatmentEfficacy;
	}

	public static Double getOneMinusProdTreatmentEfficacy() {
		return oneMinusProdTreatmentEfficacy;
	}

	public static void setOneMinusProdTreatmentEfficacy(
			Double oneMinusProdTreatmentEfficacy) {
		Parameters.oneMinusProdTreatmentEfficacy = oneMinusProdTreatmentEfficacy;
	}

	public static String getFulToDeadMuName() {
		return fulToDeadMuName;
	}

	public static void setFulToDeadMuName(String fulToDeadMuName) {
		Parameters.fulToDeadMuName = fulToDeadMuName;
	}

	public static String getFulToDeadSigmaName() {
		return fulToDeadSigmaName;
	}

	public static void setFulToDeadSigmaName(String fulToDeadSigmaName) {
		Parameters.fulToDeadSigmaName = fulToDeadSigmaName;
	}

	public static String getProdToFulMuName() {
		return prodToFulMuName;
	}

	public static void setProdToFulMuName(String prodToFulMuName) {
		Parameters.prodToFulMuName = prodToFulMuName;
	}

	public static String getProdToFulSigmaName() {
		return prodToFulSigmaName;
	}

	public static void setProdToFulSigmaName(String prodToFulSigmaName) {
		Parameters.prodToFulSigmaName = prodToFulSigmaName;
	}

	public static String getAsxToProdMuName() {
		return asxToProdMuName;
	}

	public static void setAsxToProdMuName(String asxToProdMuName) {
		Parameters.asxToProdMuName = asxToProdMuName;
	}

	public static String getAsxToProdSigmaName() {
		return asxToProdSigmaName;
	}

	public static void setAsxToProdSigmaName(String asxToProdSigmaName) {
		Parameters.asxToProdSigmaName = asxToProdSigmaName;
	}

}
