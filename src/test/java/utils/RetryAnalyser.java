package utils;


import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
// Retry Analyzer to implement retry logic

public class RetryAnalyser implements IRetryAnalyzer {
	private int retryCount = 0;
	private static final int maxRetryCount = 1; // Number of retries allowed

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			retryCount++;
			return true; // Retry test
		}
		return false; // Stop retrying
	}
}