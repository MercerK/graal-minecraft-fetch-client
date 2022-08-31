// MIT License

// Copyright (c) 2020 CraftJS https://github.com/Dysfold/craftjs

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
package fetch;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class FetchClient {
	public HttpClient httpClient = HttpClient.newHttpClient();
	public Plugin plugin;

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * @author: CraftJS
	 */
	private void runSync(Runnable task) {
		if (this.plugin instanceof Plugin) {
			Bukkit.getScheduler().runTask(plugin, task);
		} else {
			throw new Error("Plugin is undefined. You must set the plugin by using `setPlugin`.");
		}
	}

	/**
	 * @author: CraftJS
	 */
	public Thenable fetch(String uri, String method, String payload, String[] headers) {
		// Prepare request
		BodyPublisher body = payload != null ? BodyPublishers.ofString(payload) : BodyPublishers.noBody();
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.method(method, body);
		if (headers.length > 0) {
			builder = builder.headers(headers);
		}
		HttpRequest request = builder.build();

		// Return JS object that can be used for creating a promise
		// We're collecting entire response body to string before resolving
		// In future, headers should be made available before body is awaited
		return (resolve, reject) -> {
			httpClient.sendAsync(request, BodyHandlers.ofString())
					.whenComplete((response, e) -> {
						// GraalJS is not thread safe, so call promise on server thread
						if (e != null) {
							this.runSync(() -> reject.execute(e));
						} else {
							this.runSync(() -> resolve.execute(response));
						}
					});
		};
	}
}
