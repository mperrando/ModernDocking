/*
Copyright (c) 2022 Andrew Auclair

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package docking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class DockedUnpinnedPanel extends JPanel implements ComponentListener {
	private final Dockable dockable;
	private final RootDockingPanel root;
	private final DockableToolbar toolbar;

	private boolean configured = false;

	public DockedUnpinnedPanel(Dockable dockable, RootDockingPanel root, DockableToolbar toolbar) {
		this.dockable = dockable;
		this.root = root;
		this.toolbar = toolbar;

		root.addComponentListener(this);
		addComponentListener(this);

		setLayout(new BorderLayout());

		add(new DockedSimplePanel(Docking.getWrapper(dockable)), BorderLayout.CENTER);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (!configured) {
			configured = true;

			Point toolbarLocation = toolbar.getLocation();
			SwingUtilities.convertPointToScreen(toolbarLocation, toolbar.getParent());

			Dimension toolbarSize = toolbar.getSize();

			// this panel will be in a layered pane without a layout manager
			// we must configure the size and position ourselves
			if (toolbar.isVertical()) {
				int width = (int) (root.getWidth() / 4.0);
				int height = toolbarSize.height;

				Point location = new Point(toolbarLocation.x + toolbarSize.width, toolbarLocation.y);
				Dimension size = new Dimension(width, height);

				setLocation(location);
				setSize(size);
			}
			else {
				int width = toolbarSize.width;
				int height = (int) (root.getHeight() / 4.0);

				Point location = new Point(toolbarLocation.x, toolbarLocation.y - height);
				Dimension size = new Dimension(width, height);

				SwingUtilities.convertPointFromScreen(location, getParent());
				setLocation(location);
				setSize(size);
			}

			revalidate();
			repaint();
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (e.getComponent() == root) {
			Point toolbarLocation = toolbar.getLocation();
			SwingUtilities.convertPointToScreen(toolbarLocation, toolbar.getParent());

			Dimension toolbarSize = toolbar.getSize();

			if (toolbar.isVertical()) {
				Point location = new Point(toolbarLocation.x + toolbarSize.width, toolbarLocation.y);

				setLocation(location);
				setSize(getWidth(), toolbar.getHeight());
			}
			else {
				Point location = new Point(toolbarLocation.x, toolbarLocation.y - getHeight());

				SwingUtilities.convertPointFromScreen(location, getParent());

				setLocation(location);
				setSize(toolbar.getWidth(), getHeight());
			}

			revalidate();
			repaint();
		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
