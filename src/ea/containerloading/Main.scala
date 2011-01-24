package ea.containerloading

import org.uncommons.watchmaker.framework.selection._
import org.uncommons.watchmaker.framework.termination._
import org.uncommons.watchmaker.framework._
import org.uncommons.maths.random.Probability
import scala.collection.JavaConversions._
import ea.containerloading.vis._
import org.jfree.chart._
import org.jfree.data.xy._

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import java.text.{DecimalFormat}

object Main {
	
	var currentEpoch = 0
	
	def main(args : Array[String]) : Unit = {

		val problem = new ContainerProblem(
			containerSize = Dimension3D(50, 50, 50),
			boxSizeFrequencies =
				Map(Dimension3D(10,10,12) -> (5,  BoxConstraints(widthVertical = true, depthVertical = true)),
			        Dimension3D(20,10,10) -> (15, BoxConstraints(widthVertical = true, depthVertical = true)),
			        Dimension3D(30,10,20) -> (10, BoxConstraints(widthVertical = true, depthVertical = true)),
			        Dimension3D(50,10,10) -> (5,  BoxConstraints(widthVertical = true, depthVertical = true))
			        ))
	
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(10, 10, 10),
//			boxSizeFrequencies =
//				Map(Dimension3D(10,10,5) -> (2, BoxConstraints(widthVertical = true, depthVertical = true))
//					))
			
//		// thpack1 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> (40, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(43,25,110) -> (33, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(81,55,92)  -> (39, BoxConstraints(widthVertical = true, depthVertical = true))
//			        ))
		
//		// thpack2 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> (24, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(43,25,110) -> (7,  BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(81,55,92)  -> (22, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(33,28,81)  -> (13, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(99,73,120) -> (15, BoxConstraints(widthVertical = true, depthVertical = true))
//			        ))
		
//		// thpack3 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> (24, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(43,25,110) -> (9,  BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(81,55,92)  -> (8,  BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(33,28,81)  -> (11, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(99,73,120) -> (11, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(70,48,111) -> (10, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(72,46,98)  -> (12, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(66,31,95)  -> (9,  BoxConstraints(widthVertical = false, depthVertical = false))
//			        ))
		
//		// thpack7 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> (10, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(43,25,110) -> (6, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(81,55,92)  -> (5, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(33,28,81)  -> (5, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(99,73,120) -> (6, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(70,48,111) -> (4, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(72,46,98)  -> (7, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(66,31,95)  -> (6, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(84,30,85)  -> (5, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(32,25,71)  -> (5, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(34,25,36)  -> (4, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(67,62,97)  -> (8, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(25,23,33)  -> (2, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(27,26,95)  -> (5, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(81,44,94)  -> (8, BoxConstraints(widthVertical = true, depthVertical = false)),
//			        Dimension3D(39,38,41)  -> (4, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(74,65,104) -> (4, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(41,36,52)  -> (4, BoxConstraints(widthVertical = true, depthVertical = true)),
//			        Dimension3D(78,34,104) -> (6, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(77,46,83)  -> (6, BoxConstraints(widthVertical = true, depthVertical = true))
//			        ))
		
//		// thpack8 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(2000, 1000, 3000),
//			boxSizeFrequencies =
//				Map(Dimension3D(375,300,400)  -> (24, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(400,150,400)  -> (10, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(150,200,300)  -> (11, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,275,900)  -> (14, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(150,275,800)  -> (6,  BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(150,200,1500) -> (20, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,200,900)  -> (15, BoxConstraints(widthVertical = false, depthVertical = false))
//			        ))
		
//		// thpack8 - 2
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(2000, 1000, 3000),
//			boxSizeFrequencies =
//				Map(Dimension3D(375,250,400) -> (29, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(400,150,400) -> (37, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(300,200,300) -> (34, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(375,400,500) -> (19, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(275,200,800) -> (16, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(350,350,450) -> (17, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,125,200) -> (23, BoxConstraints(widthVertical = false, depthVertical = false))
//			        ))

//		// thpack8 - 4
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(2000, 1100, 3000),
//			boxSizeFrequencies =
//				Map(Dimension3D(375,200,400) -> (16, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(250,250,400) -> (23, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(300,200,300) -> (17, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(500,500,225) -> (9,  BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(400,275,800) -> (8,  BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,275,600) -> (17, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,275,900) -> (10, BoxConstraints(widthVertical = false, depthVertical = false))
//			        ))
		
//		// thpack8 - 5
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(2000, 900, 3000),
//			boxSizeFrequencies =
//				Map(Dimension3D(375,200,400) -> (35, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(250,225,300) -> (15, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(500,100,500) -> (25, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(250,250,800) -> (10, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,200,1500)-> (20, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,200,900) -> (15, BoxConstraints(widthVertical = false, depthVertical = false))
//			        ))
		
//		// thpack8 - 6
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(2400, 1000, 3500),
//			boxSizeFrequencies =
//				Map(Dimension3D(375,250,400) -> (34, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(275,225,400) -> (37, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(250,125,300) -> (23, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(450,225,500) -> (27, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(275,200,1500)-> (25, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(400,300,600) -> (23, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(200,200,900) -> (14, BoxConstraints(widthVertical = false, depthVertical = false)),
//			        Dimension3D(350,300,700) -> (17, BoxConstraints(widthVertical = false, depthVertical = false))
//			        ))
		
		val userAbort = new UserAbort
		
		val runner = new EvolutionaryContainerLoading(
			islands = Some(IslandConfig(epochLength = 10, migrantCount = 2)),
			//islands = None,
			new SigmaScaling,
			//new RankSelection,
			populationSize = 4,
			eliteCount = 0,
			crossoverProbability = Probability.EVENS,
			//new TargetFitness(0.9, true),
			new GenerationCount(3),
			userAbort
			//termination = new ElapsedTime(1*60*1000)
			)
		
		showAbortWindow(userAbort)
		
		val fitnessFormat = new DecimalFormat("0.0000")
		val meanStdDevFitnessSeries = new YIntervalSeries("Mean Fitness with " + '\u03C3')
		val maxFitnessSeries = new XYSeries("Max Fitness")
		
		val island0MeanStdDevFitness = new YIntervalSeries("Mean Fitness with " + '\u03C3' + " (Island 0)")
		val island1MeanStdDevFitness = new YIntervalSeries("Mean Fitness with " + '\u03C3' + " (Island 1)")
		
		runner.addListener(popData => {
				val evoType = if (runner.islands.isDefined) "epoch" else "generation"
				println(evoType + " " + (popData.getGenerationNumber + 1) + " - " +
						"best fitness: " + fitnessFormat.format(popData.getBestCandidateFitness) + " " + 
						"mean fitness: " + fitnessFormat.format(popData.getMeanFitness) + " " +
						"std dev: " + fitnessFormat.format(popData.getFitnessStandardDeviation) + " -- " + 
						popData.getElapsedTime / 1000 + "s so far")

				meanStdDevFitnessSeries.add(
						popData.getGenerationNumber + 1,
						popData.getMeanFitness, 
						popData.getMeanFitness - popData.getFitnessStandardDeviation,
						popData.getMeanFitness + popData.getFitnessStandardDeviation)
				
				maxFitnessSeries.add(popData.getGenerationNumber + 1, popData.getBestCandidateFitness)
				
				currentEpoch += 1
			})
			
		runner.addIslandListener((islandIndex, popData) => {
				val realGenerationNumber = 
					(popData.getGenerationNumber + 1) + 
					currentEpoch * runner.islands.get.epochLength
			
				println("island " + islandIndex + ", " + 
						"generation " + (popData.getGenerationNumber + 1) + " (" + realGenerationNumber + ") - " +
						"best fitness: " + fitnessFormat.format(popData.getBestCandidateFitness) + " " + 
						"mean fitness: " + fitnessFormat.format(popData.getMeanFitness) + " " +
						"std dev: " + fitnessFormat.format(popData.getFitnessStandardDeviation) + " -- " + 
						popData.getElapsedTime / 1000 + "s so far")
						
				islandIndex match {
					case 0 => island0MeanStdDevFitness.add(
								realGenerationNumber,
								popData.getMeanFitness,
								popData.getMeanFitness - popData.getFitnessStandardDeviation,
								popData.getMeanFitness + popData.getFitnessStandardDeviation)
					case 1 => island1MeanStdDevFitness.add(
								realGenerationNumber,
								popData.getMeanFitness,
								popData.getMeanFitness - popData.getFitnessStandardDeviation,
								popData.getMeanFitness + popData.getFitnessStandardDeviation)
				}
		})
		
		val bestBoxLoadingOrder = runner.runEvolution(problem)
		
		val meanStdDevFitnessData = new YIntervalSeriesCollection
		meanStdDevFitnessData.addSeries(meanStdDevFitnessSeries)
		val maxFitnessData = new XYSeriesCollection(maxFitnessSeries)
		val chart = createStatisticalXYLineChart(
				null, //"Container size: " + problem.container.size + ", boxes: " + problem.boxes.size,
				if (runner.islands.isDefined) "Epoch" else "Generation",
				"Fitness",
				meanStdDevFitnessData, maxFitnessData,
				if (runner.islands.isDefined) Some("Generation") else None,
				if (runner.islands.isDefined) { 
					val c = new YIntervalSeriesCollection
					c.addSeries(island0MeanStdDevFitness)
					c.addSeries(island1MeanStdDevFitness)
					Some(c)
				} else None)
		val chartFrame = new org.jfree.chart.ChartFrame("Fitness", chart)
		addSaveAsPdfMenuButton(chartFrame)
		chartFrame.pack
		chartFrame.setVisible(true)
		
		val loadingResult = ContainerLoader.loadLayer(problem.container, bestBoxLoadingOrder)
		
		println("skipped: " + loadingResult.skippedBoxes.length)
		
		val rotatedBoxes = loadingResult.loadedBoxes.filter { b =>
			b.rotation match { 
				case BoxRotation(false,false,false) => false
				case _ => true
			} }
		println("rotated boxes: " + rotatedBoxes.size)
		
		CandidateViewer.showCandidate(loadingResult)
	}
	
	private def showAbortWindow(trigger: UserAbort) = {
		val frame = new javax.swing.JFrame
		val button = new javax.swing.JButton("request abort")
		button.addActionListener(new java.awt.event.ActionListener {
			def actionPerformed(e: java.awt.event.ActionEvent) = {
				trigger.abort
			}
		})
		frame.add(button)
		frame.pack
		frame.setVisible(true)
	}
	
	private def createStatisticalXYLineChart(title: String, xLabel: String, yLabel: String, data: IntervalXYDataset, data2: XYDataset, xLabel2: Option[String], data3: Option[IntervalXYDataset]): JFreeChart = {
		
		val xAxis = new org.jfree.chart.axis.NumberAxis(xLabel)
		xAxis.setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits)
				
		val renderer = new org.jfree.chart.renderer.xy.DeviationRenderer(true, false)
		val renderer2 = new org.jfree.chart.renderer.xy.XYLineAndShapeRenderer(true, false)
		val leftPlot = new org.jfree.chart.plot.XYPlot(data2, xAxis, null, renderer2)
		leftPlot.setDataset(1, data)
		leftPlot.setRenderer(1, renderer)
		leftPlot.setOrientation(org.jfree.chart.plot.PlotOrientation.VERTICAL)
		
		renderer.setAutoPopulateSeriesPaint(false)
		renderer.setSeriesPaint(0, java.awt.Color.BLACK)
		renderer2.setAutoPopulateSeriesPaint(false)
		renderer2.setSeriesPaint(0, java.awt.Color.GREEN)
		
		val yAxis = new org.jfree.chart.axis.NumberAxis(yLabel)
		val combinedPlot = new org.jfree.chart.plot.CombinedRangeXYPlot(yAxis)
		combinedPlot.add(leftPlot, 1)
		
		if (xLabel2.isDefined && data3.isDefined) {
			val renderer3 = new org.jfree.chart.renderer.xy.DeviationRenderer(true, false)
			renderer3.setAutoPopulateSeriesPaint(false)
			renderer3.setSeriesPaint(0, java.awt.Color.RED)
			renderer3.setSeriesPaint(1, java.awt.Color.BLUE)
			renderer3.setSeriesFillPaint(0, java.awt.Color.RED)
			renderer3.setSeriesFillPaint(1, java.awt.Color.BLUE)
						
			val xAxis = new org.jfree.chart.axis.NumberAxis(xLabel2.get)
			xAxis.setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits)
			val rightPlot = new org.jfree.chart.plot.XYPlot(data3.get, xAxis, null, renderer3)
			rightPlot.setOrientation(org.jfree.chart.plot.PlotOrientation.VERTICAL)
			combinedPlot.add(rightPlot, 2)
		}

		val chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, combinedPlot, true)
		val theme = new org.jfree.chart.StandardChartTheme("JFree")
		theme.apply(chart)
		val font = new java.awt.Font("Arial Unicode MS", java.awt.Font.PLAIN, 12);
		chart.getLegend.setItemFont(font)
		chart
	}
	
	private def addSaveAsPdfMenuButton(frame: ChartFrame) = {
		val savePdfItem = new javax.swing.JMenuItem("Save as PDF...")

		savePdfItem.addActionListener(new java.awt.event.ActionListener {
			def actionPerformed(e: java.awt.event.ActionEvent) = {
				val fileChooser = new javax.swing.JFileChooser
				val filter = new org.jfree.ui.ExtensionFileFilter("PDF file", ".pdf")
				fileChooser.addChoosableFileFilter(filter)
				
				val option = fileChooser.showSaveDialog(frame)
				if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
					var filename = fileChooser.getSelectedFile.getPath
					if (!filename.endsWith(".pdf")) {
						filename += ".pdf"
					}
					saveChartAsPDF(
							new File(filename),
							frame.getChartPanel.getChart,
							Dimension2D(frame.getChartPanel.getWidth, frame.getChartPanel.getHeight))
				}
			}
		})
		
		frame.getChartPanel.getPopupMenu.add(savePdfItem)
	}
	
	private def saveChartAsPDF(file: File, chart: JFreeChart, size: Dimension2D) = {
		val mapper = new DefaultFontMapper
		val jreBinPath = System.getProperty("sun.boot.library.path")
		val jreFontsPath = jreBinPath.substring(0, jreBinPath.size - 4) + "\\lib\\fonts" 
		mapper.insertDirectory(jreFontsPath)
		val pp = mapper.getBaseFontParameters("Arial Unicode MS")
		if (pp != null) {
			pp.encoding = com.lowagie.text.pdf.BaseFont.IDENTITY_H
		}
		val out = new BufferedOutputStream(new FileOutputStream(file))
		val pagesize = new Rectangle(size.width, size.height)
		val document = new Document(pagesize, 50, 50, 50, 50)
		val writer = PdfWriter.getInstance(document, out)
		document.open
		val cb = writer.getDirectContent
		val tp = cb.createTemplate(size.width, size.height)
		val g2 = tp.createGraphics(size.width, size.height, mapper)
		val r2D = new Rectangle2D.Double(0, 0, size.width, size.height)
		chart.draw(g2, r2D)
		g2.dispose
		cb.addTemplate(tp, 0, 0)
		document.close
		out.close
	}
}
