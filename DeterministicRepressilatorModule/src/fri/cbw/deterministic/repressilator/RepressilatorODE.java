/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministic.repressilator;

import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ParameterizedODE;
import org.apache.commons.math3.ode.UnknownParameterException;

/**
 *
 * @author miha
 */
public class RepressilatorODE implements FirstOrderDifferentialEquations, ParameterizedODE {
    
    private double kTranslacija=0.0430;
    private double kDegradMono=0.0007;
    private double kTranskripcija=0.0715;
    private double kDegrad_mRNK=0.0039; 
    private double kVezavaOR2=0.0120;
    private double kCepitevOR2=0.4791;
    private double kVezavaOR3=0.0001;
    private double kCepitevOR3=0.0000;
    private double kDimer=0.0500; 
    private double kMono=0.5000;
    
    private final Collection<String> parameterNames=Arrays.asList("kTranslacija", "kDegradMono", "kTranskripcija", "kDegrad_mRNK", "kVezavaOR2", "kCepitevOR2", "kVezavaOR3", "kCepitevOR3", "kDimer", "kMono");
    
    @Override
    public double getParameter(String string) throws UnknownParameterException {
        if(string.equals("kTranslacija")) {
            return kTranslacija;
        }
        else if(string.equals("kDegradMono")) {
            return kDegradMono;
        }
        else if(string.equals("kTranskripcija")) {
            return kTranskripcija;
        }
        else if(string.equals("kDegrad_mRNK")) {
            return kDegrad_mRNK;
        }
        else if(string.equals("kVezavaOR2")) { 
            return kVezavaOR2;
        }
        else if(string.equals("kCepitevOR2")) {
            return kCepitevOR2;
        }
        else if(string.equals("kVezavaOR3")) {
            return kVezavaOR3;
        }
        else if(string.equals("kCepitevOR3")) {
            return kCepitevOR3;
        }
        else if(string.equals("kDimer")) {
            return kDimer;
        }
        else if(string.equals("kMono")) {
            return kMono;
        }
        else {
            throw new UnknownParameterException(string);
        }
    }

    @Override
    public void setParameter(String string, double d) throws UnknownParameterException {
        if(string.equals("kTranslacija")) {
            kTranslacija=d;
        }
        else if(string.equals("kDegradMono")) {
            kDegradMono=d;
        }
        else if(string.equals("kTranskripcija")) {
            kTranskripcija=d;
        }
        else if(string.equals("kDegrad_mRNK")) {
            kDegrad_mRNK=d;
        }
        else if(string.equals("kVezavaOR2")) { 
            kVezavaOR2=d;
        }
        else if(string.equals("kCepitevOR2")) {
            kCepitevOR2=d;
        }
        else if(string.equals("kVezavaOR3")) {
            kVezavaOR3=d;
        }
        else if(string.equals("kCepitevOR3")) {
            kCepitevOR3=d;
        }
        else if(string.equals("kDimer")) {
            kDimer=d;
        }
        else if(string.equals("kMono")) {
            kMono=d;
        }
        else {
            throw new UnknownParameterException(string);
        }
    }

    @Override
    public Collection<String> getParametersNames() {
        return parameterNames;
    }

    @Override
    public boolean isSupported(String string) {
        return parameterNames.contains(string);
    }

    @Override
    public int getDimension() {
        return 6;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        double alpha = kTranskripcija;
	double alpha0 = 0;
	double beta = kTranslacija;
	double gamma = 2;
	double Kd = Math.pow(kCepitevOR2/kVezavaOR2, (1/gamma)); // razmerje med cepitvijo in vezavo na operator nasprotnega proteina

	double delta_m = kDegrad_mRNK;
	double delta_x = kDegradMono;
	double delta_y = kDegradMono;
	double delta_z = kDegradMono;
        
        
	//px
        yDot[0] = beta*y[3] - delta_x * y[0];
	//py
        yDot[1] = beta*y[4] - delta_y * y[1];
	//pz
        yDot[2] = beta*y[5] - delta_z * y[2];
        
        //mx
        yDot[3] = alpha /(1+Math.pow(y[2]/Kd,gamma)) + alpha0 - y[3]*delta_m;
	//my
        yDot[4] = alpha /(1+Math.pow(y[0]/Kd,gamma)) + alpha0 - y[4]*delta_m;
	//mz
        yDot[5] = alpha /(1+Math.pow(y[1]/Kd,gamma)) + alpha0 - y[5]*delta_m;
        
    }
    
}
