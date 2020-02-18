<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:form id = "formSelezionaEntitaDiPartenza_capitolo" action="" cssClass="form-horizontal hide" novalidate="novalidate">
	<div  id="buttonSliding"  data-toggle="slidewidth" class=" fieldset-heading button-sliding" data-target="#selezioneConsultazioneEntitaCollegate">								
		<h4>
			<i id ="buttonSlidingIcon" data-original-title="" class="icon-double-angle-left sliding-icon icon-large tooltip-test defaultcolor"></i>&nbsp;									
			Selezione capitolo
		</h4>								
	</div>
	<p>Specificare l'elemento da cui iniziare la consultazione:</p>
	<h3>&nbsp;</h3>
	<!-- <div class="control-group" id="tipoCapitolo">
		<label class="control-label">Capitolo *</label>
		<div class="controls">
			<label class="radio inline"> <input type="radio" id="tipoCapitoloEntrata" data-maintain = ""  name="tipoCapitolo" value="Entrata">
				Entrata
			</label> 
			<label class="radio inline"> <input type="radio" id="tipoCapitoloSpesa" data-maintain = "" name="tipoCapitolo" value="Spesa">
				Spesa
			</label>
		</div>
	</div> -->

	<div class="control-group" id="faseBilancioCapitolo">
		<label class="control-label">Bilancio *</label>
		<div class="controls">
			<label class="radio inline"> 
				<input type="radio"  data-maintain = "" name="faseBilancio" value="PREVISIONE"> Previsione
			</label> 
			<label class="radio inline"> 
				<input type="radio" data-maintain = "" name="faseBilancio" value="GESTIONE"> Gestione
			</label>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="anno">Anno</label>
		<div class="controls">
			<input id="annoCapitolo" class="lbTextSmall span2" type="text" value=""
				name="annoCapitolo" maxlength="4" placeholder="anno" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="numeroCapitolo">Numero Capitolo</label>
		<div class="controls">
			<input type="text" id="numeroCapitolo"
				class="lbTextSmall span2 soloNumeri" name="numeroCapitolo"
				maxlength="200" placeholder="capitolo" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="numeroArticolo">Numero articolo</label>
		<div class="controls">
			<input type="text" id="numeroArticolo"
				class="lbTextSmall span2 soloNumeri"
				name="numeroArticolo" maxlength="200"
				placeholder="articolo" />
		</div>
	</div>
	<s:if test="%{gestioneUEB}">
		<div class="control-group">
			<label class="control-label" for="numeroUEB"><abbr
				title="Unit&agrave; Elementare Bilancio">UEB</abbr></label>
			<div class="controls">
				<input type="text" id="numeroUEB"
					class="lbTextSmall span2 soloNumeri" maxlength="200"
					name="numeroUEB" placeholder="UEB" />
			</div>
		</div>
	</s:if>
	<s:else>
		<s:hidden name="numeroUEB" />
	</s:else>

	<div class="control-group">
		<label class="control-label" for="descrizioneCapitolo">Descrizione</label>
		<div class="controls">
			<textarea rows="5" cols="15" id="descrizioneCapitolo" maxlength="500"
				name="descrizione" class="span10"></textarea>
		</div>
	</div>
	<div id="campiSelezioneCapitoloEntrata" class="hide">
		<div class="control-group">
			<label for="titoloEntrata" class="control-label">Titolo</label>
			<div class="controls">
				<select id="titoloEntrata" class="span10 loading-data" name="titoloEntrata.uid" disabled="disabled">
					<option value=""></option>
				</select>
					
			</div>
		</div>

		<div class="control-group">
			<label for="tipologiaTitolo" class="control-label">Tipologia
				<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
					<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare
							prima il Titolo</span>
				</i>
			</a>
			</label>
			<div class="controls">
				<select id="tipologiaTitolo" class="span10" name="tipologiaTitolo.uid" disabled="disabled" data-maintain="">
					<option value = "0"></option>
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label for="categoriaTipologiaTitolo" class="control-label">Categoria
				<a class="tooltip-test" title="selezionare prima la Tipologia"
				href="#"> <i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare prima la Tipologia</span>
				</i>
			</a>
			</label>
			<div class="controls">
				<select id="categoriaTipologiaTitolo" class="span10" name="categoriaTipologiaTitolo.uid" data-maintain="" disabled="disabled">
					<option value="0"></option>
				</select>
			</div>
		</div>		
	</div> 


<!------------  ********* CAMPI CAPITOLO SPESA ********* ------------->
	<div id="campiSelezioneCapitoloSpesa" class="hide">
		<div class="control-group">
			<label for="titoloSpesa" class="control-label">Titolo</label>
			<div class="controls">
				<select id="titoloSpesa" class="span10 loading-data" name="titoloSpesa.uid" disabled="disabled">
					<option value=""></option>
				</select>
			</div>
		</div>		
		<div class="control-group">
			<label for="macroaggregato" class="control-label">Macroaggregato
				<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
					<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare
							prima il Titolo spesa</span>
				</i>
			</a>
			</label>
			<div class="controls">
				<select id="macroaggregato" name="macroaggregato.uid" class="span10" data-maintain="" disabled="disabled">
					<option value="0"></option>
				</select>
			</div>
		</div>		
	</div>
	<div class="control-group span11">
		<label class="control-label">Struttura Amministrativa</label>
		<div class="controls">
			<div class="accordion struttAmm">
				<div class="accordion-group">
					<div class="accordion-heading">
						<a class="accordion-toggle"
							id="accordionPadreStrutturaAmministrativa" href="#struttAmmCapitolo">
							<span id="SPAN_StrutturaAmministrativoContabile_treeStruttAmmCapitolo">
								Seleziona la Struttura amministrativa 
							</span> 
							<i class="icon-spin icon-refresh spinner"></i>
						</a>
					</div>
					<div id="struttAmmCapitolo" class="accordion-body collapse">
						<div class="accordion-inner">
							<ul id="treeStruttAmmCapitolo" class="ztree treeStruttAmm"></ul>
							<button type="button" id="pulsanteDeselezionaStrutturaAmministrativoContabile_treeStruttAmmCapitolo" class="btn">Deseleziona</button>
						</div>
					</div>
				</div>
			</div>

			<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_treeStruttAmmCapitolo_uid" name="strutturaAmministrativoContabile.uid" />
			
		</div>
	</div>

	<div class="control-group">
		<s:submit id = "pulsanteRicercaEntitaDiPartenzaCapitolo" cssClass="btn btn-primary btn-block" data-maintain = "" value="cerca" />
	</div>
</s:form>