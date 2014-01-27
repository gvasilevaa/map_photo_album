/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.despark.f1rst.imagefetcher;

import com.despark.f1rst.imagefetcher.ImageCache.ImageCacheParams;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;


/**
 * Class containing some static utility methods.
 */
public class Utils {
	
	private static final String IMAGE_CACHE_DIR = "imageFetcher";
	private Utils() {
	};

	@TargetApi(11)
	public static void enableStrictMode(Class<?> strictClass) {
		if (Utils.hasGingerbread()) {
			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
			StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

			if (Utils.hasHoneycomb()) {
				threadPolicyBuilder.penaltyFlashScreen();
				vmPolicyBuilder.setClassInstanceLimit(strictClass, 1).setClassInstanceLimit(strictClass, 1);
			}
			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
			StrictMode.setVmPolicy(vmPolicyBuilder.build());
		}
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
	
	public static ImageFetcher getImageFetcher(final FragmentActivity activity) {
		ImageCacheParams cacheParams = new ImageCacheParams(activity, IMAGE_CACHE_DIR);
		// Set memory cache to 25% of mem class
		cacheParams.setMemCacheSizePercent(activity, 0.25f);
		// The ImageFetcher takes care of loading images into our ImageView children asynchronously
		ImageFetcher imageFetcher = new ImageFetcher(activity);
		imageFetcher.setImageFadeIn(true);
		imageFetcher.addImageCache(activity.getSupportFragmentManager(), cacheParams);
		return imageFetcher;
	}
}