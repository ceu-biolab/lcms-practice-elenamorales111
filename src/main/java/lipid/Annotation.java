package lipid;

import adduct.Adduct;
import adduct.AdductList;

import java.sql.SQLOutput;
import java.util.*;

/**
 * Class to represent the annotation over a lipid
 */
public class Annotation {

    private final Lipid lipid;
    private final double mz;
    private final double intensity; // intensity of the most abundant peak in the groupedPeaks
    private final double rtMin;
    private final IoniationMode ionizationMode;
    private String adduct; // !!TODO The adduct will be detected based on the groupedSignals
    private final Set<Peak> groupedSignals;
    private int score;
    private int totalScoresApplied;
    private static double PPMTOLERANCE=0.01;


    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode) {
        this(lipid, mz, intensity, retentionTime, ionizationMode, Collections.emptySet());
    }

    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     * @param groupedSignals
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode, Set<Peak> groupedSignals) {
        this.lipid = lipid;
        this.mz = mz;
        this.rtMin = retentionTime;
        this.intensity = intensity;
        this.ionizationMode = ionizationMode;
        // !!TODO This set should be sorted according to help the program to deisotope the signals plus detect the adduct
        this.groupedSignals = new TreeSet<>(groupedSignals);
        this.score = 0;
        this.totalScoresApplied = 0;

        this.adduct = detectAdduct(this.groupedSignals, this.PPMTOLERANCE);
    }

    public Lipid getLipid() {
        return lipid;
    }

    public double getMz() {
        return mz;
    }

    public double getRtMin() {
        return rtMin;
    }

    public String getAdduct() {
        return adduct;
    }

    public void setAdduct(String adduct) {
        this.adduct = adduct;
    }

    public double getIntensity() {
        return intensity;
    }

    public IoniationMode getIonizationMode() {
        return ionizationMode;
    }

    public Set<Peak> getGroupedSignals() {
        return Collections.unmodifiableSet(groupedSignals);
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // !CHECK Take into account that the score should be normalized between -1 and 1
    public void addScore(int delta) {
        this.score += delta;
        this.totalScoresApplied++;
    }

    /**
     * @return The normalized score between 0 and 1 that consists on the final number divided into the times that the rule
     * has been applied.
     */
    public double getNormalizedScore() {
        return (double) this.score / this.totalScoresApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;
        Annotation that = (Annotation) o;
        return Double.compare(that.mz, mz) == 0 &&
                Double.compare(that.rtMin, rtMin) == 0 &&
                Objects.equals(lipid, that.lipid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lipid, mz, rtMin);
    }

    @Override
    public String toString() {
        /* return String.format("Annotation(%s, mz=%.4f, RT=%.2f, adduct=%s, intensity=%.1f, score=%d)",
                lipid.getName(), mz, rtMin, adduct, intensity, score); */
        return String.format("Annotation(%s, mz=%.4f, RT=%.2f, adduct=%s, intensity=%.1f, score=%d)",
                lipid, mz, rtMin, adduct, intensity, score);			
    }


    // !!TODO Detect the adduct with an algorithm or with drools, up to the user.
    /**
     * Detects the adduct that corresponds to a set of peaks
     *
     * @param peaks Set of experimental peaks
     * @param theoreticalMass Monoisotopic mass of the lipid
     * @param ppmTolerance Error margin that can occur in ppm
     * @return the adduct string if the error margin is valid, if not then null is returned
     */
    public static String detectAdduct(Set<Peak> peaks, double ppmTolerance) {


        Map<String, Double> allAdducts = new LinkedHashMap<>();
        allAdducts.putAll(AdductList.MAPMZPOSITIVEADDUCTS);
        allAdducts.putAll(AdductList.MAPMZNEGATIVEADDUCTS);

        for (Peak peak : peaks) {
            double mz = peak.getMz();
            //System.out.println("Mz of peak: "+mz);


            for (Map.Entry<String, Double> entry : allAdducts.entrySet()) {
                String adduct = entry.getKey();
                //System.out.println("Adduct of the table: "+adduct + " " + entry.getValue());

                double expectedMz = Adduct.getMonoisotopicMassFromMZ(mz, adduct);
                //System.out.println("Expected MZ: "+expectedMz);

                double ppm = mz - expectedMz - Math.abs(entry.getValue());
                //System.out.println("ppm: "+ppm);

                if (ppm <= ppmTolerance) {

                    //System.out.println("Detected adduct: "+adduct);
                    return adduct;
                }
            }
        }

        return null;
    }

}




