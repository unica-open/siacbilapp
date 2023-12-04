<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:set var="suffix"><c:out value="${param.suffix}" default="" /></s:set>

<div class="clear"></div>
<h5>Stanziamenti</h5>
<fieldset id="fielsetInserimentoImporti<s:property value="#suffix"/>" class="form-horizontal">
	<div class="control-group">
		<label class="control-label" for="competenzaVariazioneAnno0<s:property value="#suffix"/>">Competenza ${annoEsercizioInt}</label>
		<div class="controls">
			<s:textfield id="competenzaVariazioneAnno0%{#suffix}" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="competenzaVariazione" />
			<span class="al">
				<label class="radio inline" for="residuoVariazione<s:property value="#suffix"/>">&nbsp;&nbsp;&nbsp;Residuo ${annoEsercizioInt}</label>
			</span>
			<s:textfield id="residuoVariazione%{#suffix}" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="residuoVariazione" />
			<span class="al">
				<label class="radio inline" for="cassaVariazione<s:property value="#suffix"/>">Cassa ${annoEsercizioInt}</label>
			</span>
			<s:textfield id="cassaVariazione%{#suffix}" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="cassaVariazione" />
			<%-- SIAC-8262 --%>
			<span class="al">
				<a class="tooltip-test" style="padding-left: 1%" data-original-title="Calcolo automatico cassa">
					<i class="icon-info-sign">&nbsp;
						<span class="nascosto">Calcolo automatico cassa</span>
					</i>
				</a>
				<a href="#" class="btn btn-primary" id="applicaQuoadraturaCassaStanzRes<s:property value="#suffix"/>">
					<i class="icon-backward" aria-hidden="true"></i>
				</a>
			</span>
			<%-- SIAC-8262 --%>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="competenzaVariazioneAnno1<s:property value="#suffix"/>">Competenza ${annoEsercizioInt + 1}</label>
		<div class="controls">
			<s:textfield id="competenzaVariazioneAnno1%{#suffix}" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="competenzaVariazione1" />
			<span class="al">
				<label class="radio inline" for="competenzaVariazioneAnno2%<s:property value="#suffix"/>">Competenza ${annoEsercizioInt + 2}</label>
			</span>
			<s:textfield id="competenzaVariazioneAnno2%{#suffix}" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="competenzaVariazione2" />
		</div>
	</div>
</fieldset>