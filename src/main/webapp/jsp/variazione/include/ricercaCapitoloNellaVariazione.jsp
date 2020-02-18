<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<p>
	<a class="btn" href="#collapse_ricerca_nella_variazione" data-toggle="collapse" id="pulsanteApriRicercaCapitoloNellaVariazione">ricerca capitolo associato a variazione</a>
</p>
<div id="collapse_ricerca_nella_variazione" class="collapse">
	<h4>Ricerca capitolo nella variazione</h4>
	<fieldset class="form-horizontal">
		<div class="control-group">
			<span class="control-label">Capitolo</span>
			<div class="controls">
				<label class="radio inline">
					<input type="radio" name="specificaImporti.tipoCapitoloNellaVariazione" id="tipoCapitoloNellaVariaizoneRadio1" value="Entrata">&nbsp;Entrata
				</label>
				<label class="radio inline">
					<input type="radio" name="specificaImporti.tipoCapitoloNellaVariazione" id="tipoCapitoloNellaVariazioneRadio2" value="Uscita">&nbsp;Spesa
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="annoCapitoloNellaVariazione">Anno</label>
			<div class="controls">
				<s:textfield id="annoCapitoloNellaVariazione" cssClass="lbTextSmall span2 soloNumeri" value="%{annoEsercizioInt}" disabled="true" />
				<s:hidden name="specificaImporti.annoCapitoloNellaVariazione" />
				<span class="al">
					<label class="radio inline" for="numeroCapitoloNellaVariazione">Capitolo *</label>
				</span>
				<s:textfield id="numeroCapitoloNellaVariazione" cssClass="lbTextSmall span2 soloNumeri" name="specificaImporti.numeroCapitoloNellaVariazione" required="true" maxlength="9" />
				<span class="al">
					<label class="radio inline" for="numeroArticoloNellaVariazione">Articolo *</label>
				</span>
				<s:textfield id="numeroArticoloNellaVariazione" cssClass="lbTextSmall span2" name="specificaImporti.numeroArticoloNellaVariazione" required="true" maxlength="9" />
				<a href="#editStanziamenti" type = "button" class="btn btn-primary" type="button" id="pulsanteRicercaCapitoloNellaVariazione">
					<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloNellaVariazione"></i>
				</a>
			</div>
		</div>
		
	</fieldset>
</div>