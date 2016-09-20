/**
 * Copyright (c) 2000-2016 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.ajax.redirect.fails.silently.when.extension.is.used.reproducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;


/**
 * @author  Kyle Stiemann
 */
public class PartialViewContextExtensionImpl extends PartialViewContextWrapper {

	// Private Data Members
	private PartialViewContext wrappedPartialViewContext;

	public PartialViewContextExtensionImpl(PartialViewContext partialViewContext) {
		this.wrappedPartialViewContext = partialViewContext;
	}

	@Override
	public PartialResponseWriter getPartialResponseWriter() {
		return new PartialResponseWriterExtensionImpl(super.getPartialResponseWriter());
	}

	@Override
	public PartialViewContext getWrapped() {
		return wrappedPartialViewContext;
	}

	@Override
	public void setPartialRequest(boolean isPartialRequest) {
		wrappedPartialViewContext.setPartialRequest(isPartialRequest);
	}

	private static class PartialResponseWriterExtensionImpl extends PartialResponseWriter {

		// Private Data Members
		private boolean extensionWritten;
		private PartialResponseWriter wrappedPartialResponseWriter;

		public PartialResponseWriterExtensionImpl(PartialResponseWriter partialResponseWriter) {

			super(partialResponseWriter);
			this.wrappedPartialResponseWriter = partialResponseWriter;
		}

		@Override
		public void delete(String targetId) throws IOException {
			wrappedPartialResponseWriter.delete(targetId);
		}

		@Override
		public void endDocument() throws IOException {
			wrappedPartialResponseWriter.endDocument();
		}

		@Override
		public void endError() throws IOException {
			wrappedPartialResponseWriter.endError();
		}

		@Override
		public void endEval() throws IOException {
			wrappedPartialResponseWriter.endEval();
		}

		@Override
		public void endExtension() throws IOException {

			wrappedPartialResponseWriter.endExtension();
			extensionWritten = false;
		}

		@Override
		public void endInsert() throws IOException {
			wrappedPartialResponseWriter.endInsert();
		}

		@Override
		public void endUpdate() throws IOException {
			wrappedPartialResponseWriter.endUpdate();
		}

		@Override
		public void redirect(String url) throws IOException {

			writeExtensionIfNecessary();

			wrappedPartialResponseWriter.redirect(url);
		}

		@Override
		public void startDocument() throws IOException {
			wrappedPartialResponseWriter.startDocument();
		}

		@Override
		public void startError(String errorName) throws IOException {

			writeExtensionIfNecessary();

			wrappedPartialResponseWriter.startError(errorName);
		}

		@Override
		public void startEval() throws IOException {

			writeExtensionIfNecessary();

			wrappedPartialResponseWriter.startEval();
		}

		@Override
		public void startExtension(Map<String, String> attributes) throws IOException {

			if (!extensionWritten) {

				FacesContext facesContext = FacesContext.getCurrentInstance();
				attributes.put("parameterNamespace", facesContext.getViewRoot().getContainerClientId(facesContext));
			}

			wrappedPartialResponseWriter.startExtension(attributes);
		}

		@Override
		public void startInsertAfter(String targetId) throws IOException {
			wrappedPartialResponseWriter.startInsertAfter(targetId);
		}

		@Override
		public void startInsertBefore(String targetId) throws IOException {
			wrappedPartialResponseWriter.startInsertBefore(targetId);
		}

		@Override
		public void startUpdate(String targetId) throws IOException {

			writeExtensionIfNecessary();

			wrappedPartialResponseWriter.startUpdate(targetId);
		}

		@Override
		public void updateAttributes(String targetId, Map<String, String> attributes) throws IOException {
			wrappedPartialResponseWriter.updateAttributes(targetId, attributes);
		}

		private void writeExtensionIfNecessary() throws IOException {

			if (!extensionWritten) {

				Map<String, String> attributes = new HashMap<String, String>();
				FacesContext facesContext = FacesContext.getCurrentInstance();
				attributes.put("parameterNamespace", facesContext.getViewRoot().getContainerClientId(facesContext));
				wrappedPartialResponseWriter.startExtension(attributes);
				wrappedPartialResponseWriter.endExtension();
				extensionWritten = true;
			}
		}
	}
}
