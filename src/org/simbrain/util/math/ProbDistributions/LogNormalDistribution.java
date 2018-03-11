package org.simbrain.util.math.ProbDistributions;

import org.simbrain.util.UserParameter;
import org.simbrain.util.math.ProbabilityDistribution;

import umontreal.iro.lecuyer.probdist.Distribution;
import umontreal.iro.lecuyer.probdist.LognormalDist;
import umontreal.iro.lecuyer.randvar.LognormalGen;

public class LogNormalDistribution extends ProbabilityDistribution {

    @UserParameter(
            label = "Location (\\u03BC)",
            description = "The mean of the logarithm of this distribution.",
            defaultValue = "1.0", order = 1)
    private double location = 1.0;

    @UserParameter(
            label = "Scale (\\u03C3)",
            description = "The standard deviation of the logarithm of this distribution.",
            defaultValue = "0.5", order = 2)
    private double scale = 0.5;

    /**
     * For all but uniform, upper bound is only used in conjunction with
     * clipping, to truncate the distribution. So if clipping is false this
     * value is not used.
     */
    @UserParameter(
            label = "Floor",
            description = "An artificial minimum value set by the user.",
            defaultValue = "0.0", order = 3)
    private double floor = 0.0;

    /**
     * For all but uniform, lower bound is only used in conjunction with
     * clipping, to truncate the distribution. So if clipping is false this
     * value is not used.
     */
    @UserParameter(
            label = "Ceiling",
            description = "An artificial minimum value set by the user.",
            defaultValue = "" + Double.POSITIVE_INFINITY, order = 4)
    private double ceil = Double.POSITIVE_INFINITY;

    @UserParameter(
            label = "Clipping",
            description = "When clipping is enabled, the randomizer will reject outside the floor and ceiling values.",
            defaultValue = "false", order = 5)
    private boolean clipping = false;

    @Override
    public double nextRand() {
        return clipping(
                LognormalGen.nextDouble(DEFAULT_RANDOM_STREAM, location, scale),
                floor,
                ceil
                );
    }

    @Override
    public int nextRandInt() {
        return (int) nextRand();
    }

    @Override
    public LogNormalDistribution deepCopy() {
        LogNormalDistribution cpy = new LogNormalDistribution();
        cpy.location = this.location;
        cpy.scale = this.scale;
        cpy.ceil = this.ceil;
        cpy.floor = this.floor;
        cpy.clipping = this.clipping;
        return cpy;
    }

    @Override
    public String getName() {
        return "Log-Normal";
    }

    public double getLocation() {
        return location;
    }

    public double getScale() {
        return scale;
    }

    public void setLocation(double location) {
        this.location = location;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Distribution getBestFit(double[] observations, int numObs) {
        return LognormalDist.getInstanceFromMLE(observations, numObs);
    }

    public double[] getBestFitParams(double[] observations, int numObs) {
        return LognormalDist.getMLE(observations, numObs);
    }

    @Override
    public void setClipping(boolean clipping) {
        this.clipping = clipping;
    }

    @Override
    public void setUpperBound(double ceiling) {
        this.ceil = ceiling;
    }

    @Override
    public void setLowerbound(double floor) {
        this.floor = floor;
    }

    @Override
    public void setParam1(double p1) {
        this.location = p1;
    }

    @Override
    public void setParam2(double p2) {
        this.scale = p2;
    }

}
