package software.ulpgc.kata4;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

public class Main extends JFrame {

  public Main() {
    this.setTitle("Histogram Builder");
    this.setSize(800, 600);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.display(histogramOf(movies()));
    main.setVisible(true);
  }

  private void display(Histogram histogram) {
    this.getContentPane().add(displayOf(histogram));
    this.revalidate();
  }

  private Component displayOf(Histogram histogram) {
    return new ChartPanel(decorate(chartOf(histogram)));
  }

  private JFreeChart decorate(JFreeChart chart) {
    return chart;
  }

  private JFreeChart chartOf(Histogram histogram) {
    // Cambiado de createHistogram a createXYBarChart para aceptar XYSeries
    return ChartFactory.createXYBarChart(
      histogram.title(),
      histogram.x(), false,
      "count",
      datasetOf(histogram),
      PlotOrientation.VERTICAL,
      true, true, false
    );
  }

  private XYSeriesCollection datasetOf(Histogram histogram) {
    XYSeriesCollection collection = new XYSeriesCollection();
    collection.addSeries(seriesOf(histogram));
    return collection;
  }

  private XYSeries seriesOf(Histogram histogram) {
    XYSeries series = new XYSeries(histogram.legend());
    for (int bin : histogram) {
      series.add(bin, histogram.count(bin));
    }
    return series;
  }

  private static Stream<Movie> movies() {
    return new RemoteMovieDesealizer(MovieDeserialize::fromTsv).loadAll().limit(1000).filter(m -> m.year() >= 1900).filter(m -> m.year() <= 2025);
  }

  private static Histogram histogramOf(Stream<Movie> movies) {
    return HistogramBuilder.with(movies)
      .title("Películas por año")
      .x("Año")
      .legend("Nº películas")
      .build(Movie::year);
  }
}
