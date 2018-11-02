package com.dusten.paint.components;

import com.dusten.paint.controllers.CanvasController;
import com.dusten.paint.enums.ToolsEnum;
import com.dusten.paint.helpers.StatusSet;
import com.dusten.paint.operators.*;
import com.dusten.paint.primitives.Rectangle;
import com.dusten.paint.rasterizers.FreeDrawRasterize;
import com.dusten.paint.rasterizers.SelectionRasterize;
import com.dusten.paint.rasterizers.ShapeRasterize;
import com.dusten.paint.rasterizers.TextRasterize;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dusten Knull
 *
 * Component class based on the JavaFX {@link Canvas} component with
 * specific rules and events made to be useful specifically for Paint-MVC
 */
public class DrawableCanvas extends Canvas {

    public static final double DEFAULT_FILL_TOLERANCE = 0.1;
    public static final double DEFAULT_LINE_WEIGHT = 2.0;
    private static final double DEFAULT_FONT_WEIGHT = 10.0;

    // Rasterizer objects which draw previews for certain tools
    private SelectionRasterize selectionRasterize;
    private FreeDrawRasterize freeDrawRasterize;
    private ShapeRasterize shapeRasterize;
    private TextRasterize textRasterize;

    private CanvasController canvasController;
    private GraphicsContext context;
    private Clipboard clipboard;
    private ToolsEnum toolType;

    // Edit history and current edit index for use with undo/redo
    private List<PaintOperator> editHistory;
    private int editIndex;

    private StatusSet<Color> secondaryColorChanged;
    private StatusSet<Color> primaryColorChanged;

    private Color secondaryColor;
    private Color primaryColor;
    private Color currentColor;

    private String fontName;
    private double fontWeight;

    private double fillTolerance;
    private double lineWeight;
    private boolean isPasted;
    private boolean canClear;

    public DrawableCanvas() {

        this.canClear = true;
        this.fillTolerance = DEFAULT_FILL_TOLERANCE;
        this.lineWeight = DEFAULT_LINE_WEIGHT;
        this.fontWeight = DEFAULT_FONT_WEIGHT;

        this.editHistory = new ArrayList<>();
        this.editIndex = -1;

        this.selectionRasterize = new SelectionRasterize();
        this.freeDrawRasterize = new FreeDrawRasterize();
        this.shapeRasterize = new ShapeRasterize();
        this.textRasterize = new TextRasterize();

        this.isPasted = false;
        this.toolType = null;

        this.context = this.getGraphicsContext2D();
        this.context.setStroke(Color.BLACK);
        this.context.setFill(Color.BLACK);

        this.createAndSetEvents();
    }

    /**
     * Initial setup for all mouse events handled by the canvas
     */
    private void createAndSetEvents() {

        this.setFocusTraversable(true);
        this.addEventFilter(MouseEvent.ANY, (event) -> this.requestFocus());

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

            if(this.toolType == null){

                event.consume();
                return;
            }

            if(event.isPrimaryButtonDown())
                this.currentColor = this.primaryColor;
            else if(event.isSecondaryButtonDown())
                this.currentColor = this.secondaryColor;

            else {

                event.consume();
                return;
            }

            if(this.textRasterize.isTextPlaced()) {

                this.addEdit(new TextOperator(this.primaryColor, this.textRasterize.getText(),
                        Font.font(this.fontName, this.fontWeight), this.textRasterize.getPlacedAt()));
                this.textRasterize.placeText();
            }

            switch(this.toolType) {

                case LINE: // ordinal 0
                    this.shapeRasterize.setLine(currentColor, event.getX(), event.getY());
                    break;

                case ARC: // ordinal 1
                    this.shapeRasterize.setArc(currentColor, event.getX(), event.getY());
                    break;

                case RECTANGLE_DRAW: // ordinal 6
                case RECTANGLE_FILL: // ordinal 5
                    this.shapeRasterize.setRectangle(currentColor, event.getX(), event.getY());
                    break;

                case ELLIPSE_DRAW: // ordinal 8
                case ELLIPSE_FILL: // ordinal 7
                    this.shapeRasterize.setEllipse(currentColor, event.getX(), event.getY());
                    break;

                case PENCIL: // ordinal 3
                case PAINTBRUSH: // ordinal 4
                    this.freeDrawRasterize.resetStroke(this.context, currentColor, this.toolType.equals(ToolsEnum.PAINTBRUSH),
                            this.lineWeight, event.getX(), event.getY());
                    break;

                case ERASER: // ordinal 11
                    currentColor = event.isPrimaryButtonDown() ? this.secondaryColor : this.primaryColor;
                    this.freeDrawRasterize.resetStroke(this.context, currentColor, false,
                            this.lineWeight, event.getX(), event.getY());
                    break;

                case SELECT_AREA: // ordinal 9
                    this.selectionRasterize.setSelectedRectangle(this.context, event.getX(), event.getY());
                    this.isPasted = false;
                    break;

                case MOVE_AREA: // ordinal 10
                    this.selectionRasterize.setMoveSelection(this.getSnapshot(), event.getX(), event.getY());
                    break;

                default:
                    event.consume();
                    break;
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {

            if(this.toolType == null){

                event.consume();
                return;
            }

            switch(this.toolType) {

                case LINE: // ordinal 0
                    this.redraw();
                    this.shapeRasterize.renderLine(this.context, this.lineWeight, event.getX(), event.getY());
                    break;

                case ARC: // ordinal 1
                    this.redraw();
                    this.shapeRasterize.renderArc(this.context, this.lineWeight, event.getX(), event.getY());
                    break;

                case RECTANGLE_FILL: // ordinal 5
                    this.redraw();
                    this.shapeRasterize.renderFillRectangle(this.context, event.getX(), event.getY());
                    break;

                case RECTANGLE_DRAW: // ordinal 6
                    this.redraw();
                    this.shapeRasterize.renderDrawRectangle(this.context, this.lineWeight, event.getX(), event.getY());
                    break;

                case ELLIPSE_FILL: // ordinal 7
                    this.redraw();
                    this.shapeRasterize.renderFillEllipse(this.context, event.getX(), event.getY());
                    break;

                case ELLIPSE_DRAW: // ordinal 8
                    this.redraw();
                    this.shapeRasterize.renderDrawEllipse(this.context, this.lineWeight, event.getX(), event.getY());
                    break;

                case PAINTBRUSH: // ordinal 4
                case PENCIL: // ordinal 3
                case ERASER: // ordinal 11
                    this.redraw();
                    this.freeDrawRasterize.drawNextStroke(event.getX(), event.getY());
                    break;

                case SELECT_AREA: // ordinal 9
                    this.redraw();
                    this.selectionRasterize.renderSelectedRectangle(this.context, event.getX(), event.getY());
                    break;

                case MOVE_AREA: // ordinal 10
                    this.redraw();
                    this.selectionRasterize.renderMoveSelection(this.context, this.secondaryColor,
                            event.getX(), event.getY(), !this.isPasted);
                    break;

                default:
                    event.consume();
                    break;
            }
        });
        
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

            if(this.toolType == null) {

                event.consume();
                return;
            }

            switch(this.toolType) {

                case LINE: // ordinal 0
                    this.addEdit(new LineOperator(this.shapeRasterize.getLine(), this.lineWeight));
                    break;

                case ARC: // ordinal 1
                    this.addEdit(new ArcOperator(this.shapeRasterize.getArc(), this.lineWeight));
                    break;

                case RECTANGLE_FILL: // ordinal 5
                    this.addEdit(new RectangleOperator(this.shapeRasterize.getRectangle(), null));
                    break;

                case RECTANGLE_DRAW: // ordinal 6
                    this.addEdit(new RectangleOperator(this.shapeRasterize.getRectangle(), this.lineWeight));
                    break;

                case ELLIPSE_FILL: // ordinal 7
                    this.addEdit(new EllipseOperator(this.shapeRasterize.getEllipse(), null));
                    break;

                case ELLIPSE_DRAW: // ordinal 8
                    this.addEdit(new EllipseOperator(this.shapeRasterize.getEllipse(), this.lineWeight));
                    break;

                case PAINTBRUSH: // ordinal 4
                case PENCIL: // ordinal 3
                case ERASER: // ordinal 11
                    this.addEdit(new FreeDrawOperator(this.freeDrawRasterize.getStroke(), this.freeDrawRasterize.getDrawPoints(),
                            this.lineWeight, this.freeDrawRasterize.isBrush(), this.getWidth(), this.getHeight()));
                    break;

                case MOVE_AREA: // ordinal 10
                    this.addEdit(new MoveOperator(this.selectionRasterize.getSourceImage(),
                            this.selectionRasterize.getSelectedArea(), this.selectionRasterize.getDest(), !this.isPasted));
                    break;

                default:
                    event.consume();
                    break;
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            if(this.toolType == null || this.currentColor == null) {

                event.consume();
                return;
            }

            switch(this.toolType) {

                case BUCKET: // ordinal 2
                    double mouseX = event.getX();
                    double mouseY = event.getY();

                    double width = this.getWidth();
                    double height = this.getHeight();

                    this.addEdit(new BucketOperator(this.getSnapshot(), currentColor, this.fillTolerance, mouseX, mouseY, width, height));
                    break;

                case EYEDROPPER: // ordinal 12
                    Color picked = this.getSnapshot().getPixelReader().getColor((int)event.getX(), (int)event.getY());
                    if(this.currentColor.equals(this.primaryColor))
                        this.primaryColorChanged.set(picked);
                    else
                        this.secondaryColorChanged.set(picked);
                    break;

                case TEXT: // ordinal 13
                    this.textRasterize.setTextAt(this.context, this.primaryColor,
                            Font.font(this.fontName, this.fontWeight), event.getX(), event.getY());
                    break;

                default:
                    event.consume();
                    break;
            }
        });

        this.addEventHandler(KeyEvent.KEY_TYPED, event -> {

            if(!this.textRasterize.isTextPlaced()) {

                event.consume();
                return;
            }

            if(event.getCode().equals(KeyCode.ENTER)) {

                this.addEdit(new TextOperator(this.primaryColor, this.textRasterize.getText(),
                        Font.font(this.fontName, this.fontWeight), this.textRasterize.getPlacedAt()));
                this.textRasterize.placeText();
            }
            else {

                this.redraw();

                if(event.getCode().equals(KeyCode.DELETE))
                    this.textRasterize.addToText(this.context, null);
                else
                    this.textRasterize.addToText(this.context, event.getCharacter());
            }
        });
    }

    /**
     * Adds an operator to the edit history
     *
     * @param edit {@link PaintOperator} implementation instance to add to history
     */
    private void addEdit(PaintOperator edit) {

        this.editHistory.subList(this.editIndex + 1, this.editHistory.size()).clear();
        
        this.editHistory.add(edit);
        this.editIndex ++;
        
        this.canvasController.setSaved(false);
        this.redraw();
    }

    /**
     * Redraws the entire edit history while preserving current
     * {@link GraphicsContext} status
     */
    private void redraw() {

        this.context.clearRect(0.0, 0.0, this.getWidth(), this.getHeight());
        if(this.isUndoDisabled()) return;

        double currWeight = this.context.getLineWidth();
        Paint currStroke = this.context.getStroke();
        Paint currFill = this.context.getFill();

        for(int i = 0; i <= this.editIndex; i ++) {

            this.context.setFill(this.secondaryColor);
            this.editHistory.get(i).draw(this.context);
        }

        this.context.setLineWidth(currWeight);
        this.context.setStroke(currStroke);
        this.context.setFill(currFill);
    }

    /**
     * "Un-does" the most recent edit by moving the edit history index back one,
     * if applicable (DOES NOT remove the {@link PaintOperator}
     * from the edit history list)
     */
    public void undoEdit() {

        if(this.isUndoDisabled()) return;

        this.editIndex --;
        this.redraw();

        this.canvasController.setSaved(false);
    }

    /**
     * "Re-does" the most recently undone edit by moving the edit history
     * index up one, if applicable
     */
    public void redoEdit() {

        if(this.isRedoDisabled()) return;

        this.editIndex ++;
        this.redraw();

        this.canvasController.setSaved(false);
    }

    /**
     * Copy or cut the selected area to the clipboard
     *
     * @param cut Whether or not to cut the current selection when copying
     */
    public void copySelectedArea(boolean cut) {

        Rectangle selection;

        if(this.selectionRasterize.hasSelectedArea())
            selection = this.selectionRasterize.getSelectedArea();
        else
            selection = new Rectangle(0, 0, this.getWidth(), this.getHeight());

        if(cut) {

            double cx = this.selectionRasterize.getSelectedArea().getX();
            double cy = this.selectionRasterize.getSelectedArea().getY();
            double cw = this.selectionRasterize.getSelectedArea().getWidth();
            double ch = this.selectionRasterize.getSelectedArea().getHeight();

            this.context.clearRect(cx, cy, cw, ch);
        }
        this.clipboard = new Clipboard(this.getSnapshot(), selection, cut);
    }

    /**
     * If something is in the clipboard, paste it at the top left of the canvas
     */
    public void pasteClipboard() {

        if(this.clipboard == null)
            return;

        double width = this.clipboard.getSelectedWidth();
        double height = this.clipboard.getSelectedHeight();

        this.addEdit(this.clipboard.getAsMoveOperator());
        this.redraw();

        this.selectionRasterize.setSelectedRectangle(this.context, 0.0, 0.0);
        this.selectionRasterize.renderSelectedRectangle(this.context, width, height);

        this.isPasted = true;
        this.clipboard = null;
    }

    /**
     * @return Snapshot of the current canvas as a {@link WritableImage}
     */
    public WritableImage getSnapshot() {

        WritableImage writableImage = new WritableImage((int)this.getWidth(), (int)this.getHeight());

        this.redraw();
        this.snapshot(null, writableImage);

        return writableImage;
    }

    /**
     * Loads a new image for the background and clears edit history
     *
     * @param image The new {@link Image} to be loaded
     */
    public void loadImage(@NotNull Image image) {

        this.editHistory.clear();
        this.editIndex = -1;

        this.addEdit(new ImageOperator(image, this.getWidth(), this.getHeight()));
    }

    public void setToolType(@Nullable ToolsEnum toolType) {

        this.toolType = toolType;

        if(this.toolType != null)
            this.setCursor(this.toolType.getCursor());
        else
            this.setCursor(Cursor.DEFAULT);
    }

    /**
     * Clears the entire canvas
     */
    public void clearAll() {

        // Checks for previous operand to have been 'clear' so as to avoid spam
        if(!this.canClear)
            return;

        this.canClear = false;

        this.addEdit(new ClearOperator(this.secondaryColor, this.getWidth(), this.getHeight()));
    }

    /**
     * Selects the entire canvas
     */
    public void selectAll() {

        this.selectionRasterize.setSelectedRectangle(this.context, 0.0, 0.0);
        this.selectionRasterize.renderSelectedRectangle(this.context, this.getWidth(), this.getHeight());
    }

    public void setLineWeight(double lineWeight) {
        this.lineWeight = lineWeight;
    }

    public void setFillTolerance(double fillTolerance) {
        this.fillTolerance = fillTolerance;
    }

    public void setFontWeight(double fontWeight) {
        this.fontWeight = fontWeight;
    }

    public void setFontName(@NotNull String fontName) {
        this.fontName = fontName;
    }

    public void setPrimaryColor(@NotNull Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(@NotNull Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setOnPrimaryColorChanged(@NotNull StatusSet<Color> primaryColorChanged) {
        this.primaryColorChanged = primaryColorChanged;
    }

    public void setOnSecondaryColorChanged(@NotNull StatusSet<Color> secondaryColorChanged) {

        this.secondaryColorChanged = secondaryColorChanged;
        this.canClear = true;
    }

    public void setCanvasController(@NotNull CanvasController canvasController) {
        this.canvasController = canvasController;
    }

    public boolean isUndoDisabled() {
        return this.editIndex < 0;
    }

    public boolean isRedoDisabled() {
        return this.editIndex >= this.editHistory.size() - 1;
    }
}
