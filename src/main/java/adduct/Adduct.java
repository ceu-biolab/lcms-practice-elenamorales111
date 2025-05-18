package adduct;


import java.util.regex.*;

public class Adduct {

    /**
     * adductMass value of the adducts that appear in AdductList class inside a positive adduct map and
     * a negative adduct map
     *
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return mz value
     */

    public static double getAdductMass(String adduct) {

        //Double used as a class for it to return null if nothing is found in the positive map
        Double adductMass;

        adductMass = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);

        if(adductMass == null){

            adductMass = AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);


            if(adductMass == null){

                adductMass = 0.0;

            }

        }

        return adductMass;
    }

    /**
     * Calculate the mass to search depending on the adduct hypothesis
     *
     * @param mz mz
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return the monoisotopic mass of the experimental mass mz with the adduct @param adduct
     */

    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct) {

        // !! TODO METHOD
        // !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).
        Pattern pattern = Pattern.compile("\\[(\\d*)M[+-].*](\\d*)[+-]");
        Matcher matcher = pattern.matcher(adduct);

        //If multimer and/or charge do not have coefficient, their default value is 1
        int multimer = 1;
        int charge = 1;

        //Looks for coefficients before multimer and/or charge
        if (matcher.find()) {
            if (!matcher.group(1).isEmpty()) {
                multimer = Integer.parseInt(matcher.group(1));
            }
            if (!matcher.group(2).isEmpty()) {
                charge = Integer.parseInt(matcher.group(2));
            }
        }

        double adductMass = getAdductMass(adduct);
        /*
        if Adduct is single charge the formula is M = m/z +- adductMass. Charge is 1 so it does not affect

        if Adduct is double or triple charged the formula is M = ( mz +- adductMass ) * charge

        if adduct is a dimer or multimer the formula is M =  (mz +- adductMass) / numberOfMultimer

        return monoisotopicMass;

         */

        double monoisotropicMass=0;

        switch(charge){

            //A + is written because if the charge is -, it will turn negative
            case 1 : monoisotropicMass = mz + adductMass;
                break;

            case 2:
			case 3: monoisotropicMass = (mz + adductMass)*charge;
                break;

            default: if(multimer>1){

                monoisotropicMass = (mz + adductMass)/ multimer;
            }

        }

        return monoisotropicMass;
    }

    /**
     * Calculate the mz of a monoisotopic mass with the corresponding adduct ("Inverse" process of getMonoisotopicMassFromMZ())
     *
     * @param monoisotopicMass
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return
     */
    public static Double getMZFromMonoisotopicMass(Double monoisotopicMass, String adduct) {

        // !! TODO METHOD
        // !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).
        Pattern pattern = Pattern.compile("\\[(\\d*)M[+-].*](\\d*)[+-]");
        Matcher matcher = pattern.matcher(adduct);

        //If multimer and/or charge do not have coefficient, their default value is 1
        int multimer = 1;
        int charge = 1;

        //Looks for coefficients before multimer and/or charge
        if (matcher.find()) {
            if (!matcher.group(1).isEmpty()) {
                multimer = Integer.parseInt(matcher.group(1));
            }
            if (!matcher.group(2).isEmpty()) {
                charge = Integer.parseInt(matcher.group(2));
            }
        }

        double adductMass = getAdductMass(adduct);
        /*
        if Adduct is single charge the formula is m/z = M +- adductMass. Charge is 1 so it does not affect

        if Adduct is double or triple charged the formula is mz = M/charge +- adductMass

        if adduct is a dimer or multimer the formula is mz = M * numberOfMultimer +- adductMass

        return monoisotopicMass;

         */
        double mz=0;

        switch(charge){

            //A + is written because if the charge is -, it will turn negative
            case 1 : mz = monoisotopicMass + adductMass;
                break;

            case 2:
			case 3: mz = monoisotopicMass/charge + adductMass;
                break;

            default: if(multimer>1){

                mz = monoisotopicMass * multimer + adductMass;
            }

        }

        return mz;
    }

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass Mass measured by MS
     * @param theoreticalMass Theoretical mass of the compound
     */
    public static int calculatePPMIncrement(Double experimentalMass, Double theoreticalMass) {
        int ppmIncrement;
        ppmIncrement = (int) Math.round(Math.abs((experimentalMass - theoreticalMass) * 1000000
                / theoreticalMass));

        return ppmIncrement;
    }

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param measuredMass Mass measured by MS
     * @param ppm ppm of tolerance
     */
    public static double calculateDeltaPPM(Double experimentalMass, int ppm) {
        double deltaPPM;
        deltaPPM =  Math.round(Math.abs((experimentalMass * ppm) / 1000000));
        return deltaPPM;

    }




}
