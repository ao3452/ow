/*
 * Copyright 2009-2010 Kazuyuki Shudo, and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ow.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Factory and utility methods for {@link ExecutorService ExecutorService}.
 * Provided methods return an {@link ExecutorService ExecutorService} set up
 * with commonly useful configuration settings. 
 */
public final class SingletonThreadPoolExecutors {
	public final static int NUM_THREADS_FOR_POOL = 32;
	public final static String POOLED_THREAD_NAME = "A pooled thread";
	public final static long KEEP_ALIVE_TIME = 3L;	// second

	private final static ExecutorService concurrentBlockingNonDaemonEx;
	private final static ExecutorService concurrentBlockingDaemonEx;
	private final static ExecutorService concurrentNonBlockingNonDaemonEx;
	private final static ExecutorService concurrentNonBlockingDaemonEx;
	private final static ExecutorService concurrentRejectingNonDaemonEx;
	private final static ExecutorService concurrentRejectingDaemonEx;
//	private final static ExecutorService serialBlockingNonDaemonEx;
//	private final static ExecutorService serialBlockingDaemonEx;
//	private final static ExecutorService serialNonBlockingNonDaemonEx;
//	private final static ExecutorService serialNonBlockingDaemonEx;

	static {
		// concurrent blocking
		concurrentBlockingDaemonEx =
			new ConcurrentBlockingThreadPoolExecutor(0, NUM_THREADS_FOR_POOL,
					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
					new SynchronousQueue<Runnable>(),
					new DaemonThreadFactory("Pooled thread: concurrent blocking"));

		concurrentBlockingNonDaemonEx =
			new ConcurrentBlockingThreadPoolExecutor(0, NUM_THREADS_FOR_POOL,
					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
					new SynchronousQueue<Runnable>(),
					new NonDaemonThreadFactory("Pooled thread: concurrent blocking"));

		// concurrent non-blocking
		concurrentNonBlockingDaemonEx =
			new ConcurrentNonBlockingThreadPoolExecutor(0, NUM_THREADS_FOR_POOL,
					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
					new DaemonThreadFactory("Pooled thread: concurrent non-blocking"));

		concurrentNonBlockingNonDaemonEx =
			new ConcurrentNonBlockingThreadPoolExecutor(0, NUM_THREADS_FOR_POOL,
					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
					new NonDaemonThreadFactory("Pooled thread: concurrent non-blocking"));

		// concurrent rejecting
		concurrentRejectingNonDaemonEx =
			new ThreadPoolExecutor(0, NUM_THREADS_FOR_POOL,
				KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				new NonDaemonThreadFactory("Pooled thread: concurrent rejecting"));

		concurrentRejectingDaemonEx =
			new ThreadPoolExecutor(0, NUM_THREADS_FOR_POOL,
				KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				new DaemonThreadFactory("Pooled thread: concurrent rejecting"));

		// serial blocking
//		serialBlockingDaemonEx =
//			new ThreadPoolExecutor(0, 1,
//					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
//					new SynchronousQueue<Runnable>(),
//					new DaemonThreadFactory());
//
//		serialBlockingNonDaemonEx =
//			new ThreadPoolExecutor(0, 1,
//					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
//					new SynchronousQueue<Runnable>(),
//					new NonDaemonThreadFactory());
//
//		// serial non-blocking
//		serialNonBlockingDaemonEx =
//			new ThreadPoolExecutor(0, 1,
//					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
//					new LinkedBlockingQueue<Runnable>(),
//					new DaemonThreadFactory());
//
//		serialNonBlockingNonDaemonEx =
//			new ThreadPoolExecutor(0, 1,
//					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
//					new LinkedBlockingQueue<Runnable>(),
//					new NonDaemonThreadFactory());
	}

	private final static class DaemonThreadFactory implements ThreadFactory {
		private final String threadName;

//		public DaemonThreadFactory() { this.threadName = POOLED_THREAD_NAME; }
		public DaemonThreadFactory(String threadName) { this.threadName = threadName; }

		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName(this.threadName);
			t.setDaemon(true);

			return t;
		}
	}

	private final static class NonDaemonThreadFactory implements ThreadFactory {
		private final String threadName;

//		public NonDaemonThreadFactory() { this.threadName = POOLED_THREAD_NAME; }
		public NonDaemonThreadFactory(String threadName) { this.threadName = threadName; }

		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName(this.threadName);
			t.setDaemon(false);

			return t;
		}
	}

	public static ExecutorService getThreadPool(
			ExecutorBlockingMode blockingMode, boolean daemon) {
		ExecutorService ex = null;

		switch (blockingMode) {
		case CONCURRENT_BLOCKING:
			if (daemon)
				ex = concurrentBlockingDaemonEx;
			else
				ex = concurrentBlockingNonDaemonEx;
			break;
		case CONCURRENT_NON_BLOCKING:
			if (daemon)
				ex = concurrentNonBlockingDaemonEx;
			else
				ex = concurrentNonBlockingNonDaemonEx;
			break;
		case CONCURRENT_REJECTING:
			if (daemon)
				ex = concurrentRejectingDaemonEx;
			else
				ex = concurrentRejectingNonDaemonEx;
			break;
//		case SERIAL_BLOCKING:
//			if (!daemon)
//				ex = serialBlockingNonDaemonEx;
//			else
//				ex = serialBlockingDaemonEx;
//			break;
//		case SERIAL_NON_BLOCKING:
//			if (!daemon)
//				ex = serialNonBlockingNonDaemonEx;
//			else
//				ex = serialNonBlockingDaemonEx;
//			break;
		}

		return ex;
	}
}
