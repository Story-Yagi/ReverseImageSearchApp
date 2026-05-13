package com.iyagi.reversesearch.data.remote

import com.iyagi.reversesearch.data.models.ImageResult
import org.jsoup.Jsoup

object SearchEngines {
    
    suspend fun searchGoogle(imageUrl: String): List<ImageResult> {
        return try {
            val url = "https://www.google.com/searchbyimage?image_url=$imageUrl"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(10000)
                .get()
            
            val results = mutableListOf<ImageResult>()
            doc.select("a.rg_l").forEach { element ->
                val title = element.attr("title")
                val href = element.attr("href")
                if (title.isNotEmpty() && href.isNotEmpty()) {
                    results.add(
                        ImageResult(
                            title = title,
                            url = href,
                            imageUrl = imageUrl,
                            description = "Google Images Result"
                        )
                    )
                }
            }
            results.take(10)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchYandex(imageUrl: String): List<ImageResult> {
        return try {
            val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
            val url = "https://yandex.com/images/search?rpt=imageview&url=$encodedUrl"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(10000)
                .get()
            
            val results = mutableListOf<ImageResult>()
            doc.select("a.serp-item__link").forEach { element ->
                val title = element.attr("title")
                val href = element.attr("href")
                if (title.isNotEmpty() && href.isNotEmpty()) {
                    results.add(
                        ImageResult(
                            title = title,
                            url = href,
                            imageUrl = imageUrl,
                            description = "Yandex Images Result"
                        )
                    )
                }
            }
            results.take(10)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchTwitter(imageUrl: String): List<ImageResult> {
        return try {
            val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
            val url = "https://twitter.com/search?q=$encodedUrl&f=live"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(10000)
                .get()
            
            val results = mutableListOf<ImageResult>()
            doc.select("a[href*=/status/]").forEach { element ->
                val href = element.attr("href")
                val title = element.text()
                if (href.isNotEmpty()) {
                    results.add(
                        ImageResult(
                            title = title.ifEmpty { "Twitter Post" },
                            url = "https://twitter.com$href",
                            imageUrl = imageUrl,
                            description = "Twitter/X Result"
                        )
                    )
                }
            }
            results.take(10)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchTinEye(imageUrl: String): List<ImageResult> {
        return try {
            val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
            val url = "https://tineye.com/search?url=$encodedUrl"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(10000)
                .get()
            
            val results = mutableListOf<ImageResult>()
            doc.select("div.match-box").forEach { element ->
                val link = element.selectFirst("a")
                if (link != null) {
                    val href = link.attr("href")
                    val title = link.text()
                    if (href.isNotEmpty()) {
                        results.add(
                            ImageResult(
                                title = title.ifEmpty { "TinEye Match" },
                                url = href,
                                imageUrl = imageUrl,
                                description = "TinEye Match"
                            )
                        )
                    }
                }
            }
            results.take(10)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchBing(imageUrl: String): List<ImageResult> {
        return try {
            val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
            val url = "https://www.bing.com/images/search?view=detailv2&iss=sbiupload&FORM=IRSBIQ&sbisrc=$encodedUrl"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(10000)
                .get()
            
            val results = mutableListOf<ImageResult>()
            doc.select("a.thumb").forEach { element ->
                val href = element.attr("href")
                val title = element.attr("title")
                if (href.isNotEmpty()) {
                    results.add(
                        ImageResult(
                            title = title.ifEmpty { "Bing Result" },
                            url = href,
                            imageUrl = imageUrl,
                            description = "Bing Images Result"
                        )
                    )
                }
            }
            results.take(10)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchBaidu(imageUrl: String): List<ImageResult> {
        return try {
            val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
            val url = "https://image.baidu.com/search/index?tn=baiduimage&word=$encodedUrl"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(10000)
                .get()
            
            val results = mutableListOf<ImageResult>()
            doc.select("a.imgbox").forEach { element ->
                val href = element.attr("href")
                val title = element.attr("title")
                if (href.isNotEmpty()) {
                    results.add(
                        ImageResult(
                            title = title.ifEmpty { "Baidu Result" },
                            url = href,
                            imageUrl = imageUrl,
                            description = "Baidu Images Result"
                        )
                    )
                }
            }
            results.take(10)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchAll(imageUrl: String): Map<String, List<ImageResult>> {
        return mapOf(
            "Google" to searchGoogle(imageUrl),
            "Yandex" to searchYandex(imageUrl),
            "Twitter/X" to searchTwitter(imageUrl),
            "TinEye" to searchTinEye(imageUrl),
            "Bing" to searchBing(imageUrl),
            "Baidu" to searchBaidu(imageUrl)
        )
    }
}
