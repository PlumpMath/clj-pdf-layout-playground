(ns pdf-layout.core
  (:import [com.planbase.pdf.layoutmanager BorderStyle Cell CellStyle LineStyle LogicalPage Padding PdfLayoutMgr ScaledJpeg TextStyle XyOffset]
           [org.apache.pdfbox.exceptions COSVisitorException]
           [org.apache.pdfbox.pdmodel.font PDType1Font]
                                        ;[org.junit Test]
           
           [javax.imageio ImageIO]		
           [java.awt.image BufferedImage]		
           [java.io File]		     
           [java.io FileOutputStream]		
           [java.io IOException]	 
           [java.io OutputStream]			
           [java.util Arrays]  
           [java.util List]			
           [java.awt Color]
           [com.planbase.pdf.layoutmanager CellStyle$Align]))

(let [os (FileOutputStream. "test.pdf")
      tl CellStyle$Align/TOP_LEFT
      pMargin (float 40.0)
      page-mgr (. PdfLayoutMgr newRgbPageMgr)
      logical-page (.logicalPageStart page-mgr)
      tableWidth (- (.pageWidth logical-page) (* (float 2.0) pMargin)) 
      pageRMargin (+ pMargin tableWidth)           
      colWidth (/ tableWidth (float 4.0))                    
      colWidths [(+ colWidth (float 10.0))
                 (+ colWidth (float 10.0))
                 (+ colWidth (float 10.0))
                 (- colWidth (float 30.0))]
      xya (.. logical-page
              (tableBuilder (. XyOffset (of (float 40.0) (.yPageTop logical-page))))
              (addCellWidths [(float 120.0) (float 120.0) (float 120.0)])
              (textStyle (. TextStyle (of PDType1Font/COURIER_BOLD_OBLIQUE (float 12.0)
                                          (.. Color YELLOW brighter))))
              (partBuilder)
              (cellStyle (. CellStyle (of CellStyle$Align/BOTTOM_CENTER
                                          (. Padding (of 2))
                                          (. Color (decode "#3366cc")) 
                                          (. BorderStyle (of (. Color BLACK))))))
              (rowBuilder)
              (addTextCells (into-array ["aarg" "barg" "gargle"]))
              (buildRow)
              (buildPart)

              ;; second
              (partBuilder)
              (cellStyle (. CellStyle (of CellStyle$Align/MIDDLE_CENTER
                                          (. Padding (of 2))
                                          (. Color (decode "#ccffcc")) 
                                          (. BorderStyle (of (. Color DARK_GRAY))))))
              (minRowHeight (float 120))
              (textStyle (. TextStyle (of PDType1Font/COURIER (float 12.0)
                                          (. Color BLACK))))
              (rowBuilder)
              ;; these are erroring out both the align call and the add
              ;; (cellBuilder) (align CellStyle$Align/TOP_LEFT) (add "line 1" "line two" "line three") (buildCell)
              ;; (cellBuilder) (align CellStyle$Align/TOP_CENTER) (add ["line 1" "line two" "line three"]) (buildCell)
              ;; (cellBuilder) (align CellStyle$Align/TOP_RIGHT) (add ["line 1" "line two" "line three"]) (buildCell)
              (buildRow)
              (buildPart)

              ;; finalize table
              (buildTable))]
  (.commit logical-page)
  (.save page-mgr os))



(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
