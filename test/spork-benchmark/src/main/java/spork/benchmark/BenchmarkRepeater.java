package spork.benchmark;

public class BenchmarkRepeater implements Runnable {
	private BenchmarkFactory benchmarkFactory;
	private BenchmarkResult[] benchmarkResults;

	public BenchmarkRepeater(BenchmarkFactory benchmarkFactory, int runCount) {
		this.benchmarkFactory = benchmarkFactory;
		this.benchmarkResults = new BenchmarkResult[runCount];
	}

	@Override
	public void run() {
		for (int i = 0; i < benchmarkResults.length; ++i) {
			Benchmark benchmark = benchmarkFactory.createBenchmark();
			benchmark.run();

			BenchmarkResult results = benchmark.getBenchmarkResult();
			benchmarkResults[i] = results;
		}
	}

	public BenchmarkResult[] getBenchmarkResults() {
		return benchmarkResults;
	}

	public BenchmarkResult calculateAverageBenchmarkResult() {
		double workCount = 0D;
		double workDuration = 0D;

		for (BenchmarkResult result : benchmarkResults) {
			workCount += result.getWorkCount();
			workDuration += result.getWorkDuration();
		}

		workCount /= (double)benchmarkResults.length;
		workDuration /= (double)benchmarkResults.length;

		return new BenchmarkResult(benchmarkResults[0].getBenchmarkClass(), (long)workDuration, (long)workCount);
	}

	public void printResults() {
		System.out.println("Individual results:");
		for (BenchmarkResult result : benchmarkResults) {
			System.out.println("\t" + result.toString());
		}
		System.out.println();

		System.out.println("Average results:");
		System.out.println("\t" + calculateAverageBenchmarkResult().toString());
		System.out.println();
	}
}
