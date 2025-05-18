package adduct;

public class MassTransformation {

// !! TODO create functions to transform the mass of the mzs to monoisotopic masses and vice versa.
    public static Double getMonoisotopicMassFromMz(Double mz, String adduct) {
        return Adduct.getMonoisotopicMassFromMZ(mz, adduct);
    }

    public static Double getMzFromMonoisotopicMass(Double monoMass, String adduct) {
        return Adduct.getMZFromMonoisotopicMass(monoMass, adduct);
    }
}
