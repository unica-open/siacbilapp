<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion" id="divClassificatori">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#divClassificatori" data-target="#collapseClassificatori">
				Classificatori<span class="icon">&nbsp;</span>
			</a>
		</div>
		<div id="collapseClassificatori" class="accordion-body collapse">
			<div class="accordion-inner">
				<s:iterator var="idx" begin="1" end="%{numeroClassificatoriGenerici}">
					<s:if test="%{#attr['labelClassificatoreGenerico' + #idx] != null}">
						<div class="control-group">
							<label for="classificatoreGenerico<s:property value="%{#idx}"/>" class="control-label">
								<s:property value="%{#attr['labelClassificatoreGenerico' + #idx]}"/>
							</label>
							<div class="controls">
								<s:select list="%{#attr['listaClassificatoreGenerico' + #idx]}" id="classificatoreGenerico%{#idx}"
									cssClass="span10" name="%{'classificatoreGenerico' + #idx + '.uid'}" headerKey="0" headerValue=""
									listKey="uid" listValue="%{codice + '-' + descrizione}" />
							</div>
						</div>
					</s:if>
				</s:iterator>
			</div>
		</div>
	</div>
</div>