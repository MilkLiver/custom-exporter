package test.prometheus.custom.exporter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Value("${testGaugeName}")
	String testGaugeName;

	@Value("${testGaugeLabel}")
	String testGaugeLabel;

	private final Counter requestCount;
	private final Gauge gauge;
	private final Histogram histogram;
	private final Summary summary;

	private final Gauge gauge2;
	private final Gauge testGauge01;
	private final Gauge testGauge02;
	private final Gauge testGauge03;
	private final Gauge testGauge04;
	private final Gauge testGauge05;

	private final Gauge testGauge;

	public MainController(CollectorRegistry collectorRegistry) {
		requestCount = Counter.build().name("java_request_count").help("Number of hello requests.")
				.register(collectorRegistry);
		gauge = Gauge.build().namespace("java").name("my_gauge").help("This is my gauge").register(collectorRegistry);
		histogram = Histogram.build().namespace("java").name("my_histogram").help("This is my histogram")
				.register(collectorRegistry);
		summary = Summary.build().namespace("java").name("my_summary").help("This is my summary")
				.register(collectorRegistry);

		// gauge2 =
		// Gauge.build().namespace("OwO").labelNames("label01").name("QAQ").help("Wryyyy")
		// .register(collectorRegistry);

		gauge2 = Gauge.build().namespace("OwO").labelNames("label01", "label02").name("QAQ").help("Wryyyy")
				.register(collectorRegistry);

		testGauge01 = Gauge.build().namespace("test").name("testGauge01").help("This is test testGauge01")
				.register(collectorRegistry);

		testGauge02 = Gauge.build().namespace("test").name("testGauge02").help("This is test testGauge02")
				.register(collectorRegistry);

		testGauge03 = Gauge.build().namespace("test").labelNames("label01").name("testGauge03")
				.help("This is test testGauge03").register(collectorRegistry);

		testGauge04 = Gauge.build().namespace("test").labelNames("label02").name("testGauge04")
				.help("This is test testGauge04").register(collectorRegistry);

		testGauge05 = Gauge.build().namespace("test").labelNames("label01", "label02").name("testGauge05")
				.help("This is test testGauge05").register(collectorRegistry);

		testGauge = Gauge.build().namespace("test").labelNames("name", "label").name("testGauge")
				.help("This is test testGauge").register(collectorRegistry);

	}

	protected static double rand(double min, double max) {
		return min + (Math.random() * (max - min));
	}

	@PostConstruct
	public void initial() {
		testGauge01.set(Integer.valueOf(-1));
		testGauge02.set(Integer.valueOf(-1));
		testGauge03.labels("label1").set(Integer.valueOf(-1));
		testGauge04.labels("label2").set(Integer.valueOf(-1));
		testGauge05.labels("label1", "label2").set(Integer.valueOf(-1));

		testGauge.labels(testGaugeName, testGaugeLabel).set(Integer.valueOf(-1));
	}

//	@ResponseBody
//	@GetMapping(value = "/{.*}", produces = "application/json")
//	public String getPath(HttpServletRequest request, HttpServletResponse response) {
//		log.info(request.getServletPath().toString());
//		return request.getServletPath().toString();
//	}

	@ResponseBody
	@GetMapping(value = "/test", produces = "application/json")
	public String test(HttpServletRequest request, HttpServletResponse response) {
		requestCount.inc();
		// this.counter.inc(rand(0, 5));
		gauge.set(rand(-5, 10));
		// gauge2.labels("QQ").set(rand(-5, 10));
		gauge2.labels("AA", "BB").set(rand(-5, 10));
		// gauge.set(5);
		this.histogram.observe(rand(0, 5));
		this.summary.observe(rand(0, 5));
		return "Hi!";
	}

	@ResponseBody
	@GetMapping(value = "/testGauge01_set/{setStatus}", produces = "application/json")
	public String testGauge01_set(@PathVariable("setStatus") String setStatus, HttpServletRequest request,
			HttpServletResponse response) {
		requestCount.inc();
		testGauge01.set(Integer.valueOf(setStatus));
		return "set testGauge01: " + setStatus;
	}

	@ResponseBody
	@GetMapping(value = "/testGauge02_set/{setStatus}", produces = "application/json")
	public String testGauge02_set(@PathVariable("setStatus") String setStatus, HttpServletRequest request,
			HttpServletResponse response) {
		requestCount.inc();
		testGauge02.set(Integer.valueOf(setStatus));
		return "set testGauge02: " + setStatus;
	}

	@ResponseBody
	@GetMapping(value = "/testGauge03_set/{setStatus}", produces = "application/json")
	public String testGauge03_set(@PathVariable("setStatus") String setStatus, HttpServletRequest request,
			HttpServletResponse response) {
		requestCount.inc();
		testGauge03.labels("label1").set(Integer.valueOf(setStatus));
		return "set testGauge03: " + setStatus;
	}

	@ResponseBody
	@GetMapping(value = "/testGauge04_set/{setStatus}", produces = "application/json")
	public String testGauge04_set(@PathVariable("setStatus") String setStatus, HttpServletRequest request,
			HttpServletResponse response) {
		requestCount.inc();
		testGauge04.labels("label2").set(Integer.valueOf(setStatus));
		return "set testGauge04: " + setStatus;
	}

	@ResponseBody
	@GetMapping(value = "/testGauge05_set/{setStatus}", produces = "application/json")
	public String testGauge05_set(@PathVariable("setStatus") String setStatus, HttpServletRequest request,
			HttpServletResponse response) {
		requestCount.inc();
		testGauge05.labels("label1", "label2").set(Integer.valueOf(setStatus));
		return "set testGauge05: " + setStatus;
	}

	@ResponseBody
	@GetMapping(value = "/testGauge_set/{setStatus}", produces = "application/json")
	public String testGauge_set(@PathVariable("setStatus") String setStatus, HttpServletRequest request,
			HttpServletResponse response) {
		requestCount.inc();
		testGauge.labels(testGaugeName, testGaugeLabel).set(Integer.valueOf(setStatus));
		return "set testGauge: " + setStatus;
	}

	@ResponseBody
	@PostMapping(value = { "/getWebhook", "/api/{v[1-9]}/{alerts?}" }, produces = "application/json")
	public String getWebhook(HttpServletRequest request, HttpServletResponse response) {

		log.info(request.getServletPath().toString() + " ...");

		String line;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = request.getReader().readLine()) != null) {
				sb.append(line);
			}

			if (sb.toString().replace(" ", "").equals("")) {
				log.error(request.getServletPath().toString() + " not content");
				return "meow?";
			}
			log.info(sb.toString());

			log.info(request.getServletPath().toString() + " finish");

		} catch (IOException e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			log.info(request.getServletPath().toString() + " Error");
		}

		return "receive webhook: " + sb.toString();
	}

}
