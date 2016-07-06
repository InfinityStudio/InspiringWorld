package api.simplelib.vars;

/**
 * @author ci010
 */
public interface VarNumber extends Var<Number>
{
	void increment();

	void decrement();

	void add(Number number);

	void substract(Number number);

	void multiply(Number number);

	void divide(Number number);
}
