<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset id="fieldsetCapitoloEntrata" class="form-horizontal hide">
	<s:hidden name="uid" id="HIDDEN_uidElementoCapitoloCodificheEntrata" />
	<div id="descrizioneCapitoloCapitoloEntrataDiv" class="control-group">
		<label for="descrizioneCapitoloCapitoloEntrata" class="control-label">Descrizione
			*</label>
		<div class="controls">
			<textarea rows="5" cols="15" id="descrizioneCapitoloCapitoloEntrata"
				class="span10" name="descrizioneCapitolo"></textarea>
		</div>
	</div>
	<div id="descrizioneArticoloCapitoloEntrataDiv" class="control-group">
		<label for="descrizioneArticoloCapitoloEntrata" class="control-label">Descrizione
			Articolo</label>
		<div class="controls">
			<textarea rows="5" cols="15" id="descrizioneArticoloCapitoloEntrata"
				class="span10" name="descrizioneArticolo"></textarea>
		</div>
	</div>
	<%-- <div id="titoloEntrataCapitoloEntrataDiv" class="control-group">
		<label for="titoloEntrataCapitoloEntrata" class="control-label">Titolo
			*</label>
		<div class="controls">
			<select id="titoloEntrataCapitoloEntrata" class="span10"
				name="titoloEntrata.uid"></select> <input type="hidden"
				id="titoloEntrataCapitoloEntrataCodice" name="titoloEntrata.codice" />
		</div>
	</div>
	<div id="tipologiaTitoloCapitoloEntrataDiv" class="control-group">
		<label for="tipologiaTitoloCapitoloEntrata" class="control-label">
			Tipologia * <a class="tooltip-test"
			title="selezionare prima il Titolo" href="#"><i
				class="icon-info-sign"></i></a>
		</label>
		<div class="controls">
			<select id="tipologiaTitoloCapitoloEntrata" class="span10"
				name="tipologiaTitolo.uid"></select>
		</div>
	</div>
	<div id="categoriaTipologiaTitoloCapitoloEntrataDiv"
		class="control-group">
		<label for="categoriaTipologiaTitoloCapitoloEntrata"
			class="control-label"> Categoria * <a class="tooltip-test"
			title="selezionare prima la Tipologia" href="#"><i
				class="icon-info-sign"></i></a>
		</label>
		<div class="controls">
			<select id="categoriaTipologiaTitoloCapitoloEntrata" class="span10"
				name="categoriaTipologiaTitolo.uid"></select>
		</div>
	</div>
	<div id="elementoPianoDeiContiCapitoloEntrataDiv" class="control-group">
		<label for="elementoPianoDeiContiCapitoloEntrata"
			class="control-label"> <abbr title="Piano dei Conti">P.d.C.</abbr>
			finanziario <a class="tooltip-test"
			title="selezionare prima la Categoria" href="#"><i
				class="icon-info-sign"></i></a>
		</label>
		<div class="controls">
			<input type="hidden" id="elementoPianoDeiContiCapitoloEntrata"
				name="elementoPianoDeiConti.uid" /> <a href="#" role="button"
				class="btn" data-toggle="modal"
				id="elementoPianoDeiContiCapitoloEntrataPulsante"> Seleziona il
				Piano dei Conti&nbsp; <i class="icon-spin icon-refresh spinner"
				id="elementoPianoDeiContiCapitoloEntrataSpinner"></i>
			</a>

			<div id="elementoPianoDeiContiCapitoloEntrataModale"
				class="modal hide fade" tabindex="-1" role="dialog"
				aria-labelledby="elementoPianoDeiContiCapitoloEntrataModalLabel"
				aria-hidden="true">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="elementoPianoDeiContiCapitoloEntrataModalLabel">Piano
						dei Conti</h3>
				</div>
				<div class="modal-body">
					<ul id="elementoPianoDeiContiCapitoloEntrataTree" class="ztree"></ul>
				</div>
				<div class="modal-footer">
					<button id="elementoPianoDeiContiCapitoloEntrataDeseleziona"
						class="btn">Deseleziona</button>
					<button type="button" class="btn btn-primary pull-right"
						data-dismiss="modal" aria-hidden="true">Conferma</button>
				</div>
			</div>
			&nbsp; <span id="elementoPianoDeiContiCapitoloEntrataSpan">
				Nessun P.d.C. finanziario selezionato </span>
		</div>
	</div>

	<div class="control-group">
		<label for="bottoneSIOPE" class="control-label"> <abbr
			title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
			a class="tooltip-test" title="selezionare prima il P.d.C." href="#">
				<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima il P.d.C.</span></i>
			</a
		</label>

		<div class="controls">
			<s:textfield id="siopeEntrata" data-ricerca-url="ricercaClassificatoreGerarchicoByCodice_siopeEntrata.do" name="siopeEntrata.codice"
				cssClass="span3" /> 
			&nbsp;<span id="descrizioneSiopeEntrata"><s:property
					value="siopeEntrata.descrizione" /></span>
			<s:hidden id="HIDDEN_idSiopeEntrata" name="siopeEntrata.uid" />
			<s:hidden id="HIDDEN_descrizioneSiopeEntrata"
				name="siopeEntrata.descrizione" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary"
					id="compilazioneGuidataSIOPEEntrata">compilazione guidata</button>
			</span>
		</div>
	</div> --%>
	<%-- FIXME --%>
	<!-- <input type="hidden" id="siopeEntrataCapitoloEntrata" name="siopeEntrata.uid" /> -->
	<%--div id="siopeEntrataEntrataDiv" class="control-group">
														<label for="siopeEntrataCapitoloEntrata" class="control-label">
															<abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
															<a class="tooltip-test" title="selezionare prima il P.d.C." href="#"><i class="icon-info-sign"></i></a>
														</label>
														<div class="controls">
															<input type="hidden" id="siopeEntrataCapitoloEntrata" name="siopeEntrata.uid" />
															<a href="#" role="button" class="btn" data-toggle="modal" id="siopeEntrataCapitoloEntrataPulsante">
																Seleziona il codice SIOPE&nbsp;
																<i class="icon-spin icon-refresh spinner" id="siopeEntrataCapitoloEntrataSpinner"></i>
															</a>
															<div id="siopeEntrataCapitoloEntrataModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="siopeEntrataCapitoloEntrataModalLabel" aria-hidden="true">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
																	<h3 id="siopeEntrataCapitoloEntrataModalLabel">SIOPE</h3>
																</div>
																<div class="modal-body">
																	<ul id="siopeEntrataCapitoloEntrataTree" class="ztree"></ul>
																</div>
																<div class="modal-footer">
																	<button id="siopeEntrataCapitoloEntrataDeseleziona" class="btn">Deseleziona</button>
																	<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
																</div>
															</div>
															&nbsp;
															<span id="siopeEntrataCapitoloEntrataSpan">
																	Nessun SIOPE selezionato
															</span>
														</div>
													</div--%>
	<div id="strutturaAmministrativoContabileEntrataDiv"
		class="control-group">
		<label for="strutturaAmministrativoContabileCapitoloEntrata"
			class="control-label"> Struttura Amministrativa Responsabile
		</label>
		<div class="controls">
			<input type="hidden"
				id="strutturaAmministrativoContabileCapitoloEntrata"
				name="strutturaAmministrativoContabile.uid" /> <a href="#"
				role="button" class="btn" data-toggle="modal"
				id="strutturaAmministrativoContabileCapitoloEntrataPulsante">
				Seleziona la Struttura Amministrativa&nbsp; <i
				class="icon-spin icon-refresh spinner"
				id="strutturaAmministrativoContabileCapitoloEntrataSpinner"></i>
			</a>
			<div id="strutturaAmministrativoContabileCapitoloEntrataModale"
				class="modal hide fade" tabindex="-1" role="dialog"
				aria-labelledby="strutturaAmministrativoContabileCapitoloEntrataModalLabel"
				aria-hidden="true">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="strutturaAmministrativoContabileCapitoloEntrataModalLabel">Struttura
						Amministrativa Responsabile</h3>
				</div>
				<div class="modal-body">
					<ul id="strutturaAmministrativoContabileCapitoloEntrataTree"
						class="ztree"></ul>
				</div>
				<div class="modal-footer">
					<button
						id="strutturaAmministrativoContabileCapitoloEntrataDeseleziona"
						class="btn">Deseleziona</button>
					<button type="button" class="btn btn-primary pull-right"
						data-dismiss="modal" aria-hidden="true">Conferma</button>
				</div>
			</div>
			&nbsp; <span id="strutturaAmministrativoContabileCapitoloEntrataSpan">
				Nessuna Struttura Amministrativa Responsabile selezionata </span>
		</div>
	</div>
	<%-- <div id="categoriaCapitoloCapitoloEntrataDiv" class="control-group">
		<label for="categoriaCapitoloCapitoloEntrata" class="control-label">Tipo
			Capitolo</label>
		<div class="controls">
			<select id="categoriaCapitoloCapitoloEntrata" class="span10"
				name="categoriaCapitolo.uid"></select>
		</div>
	</div>
	<div id="impegnabileCapitoloEntrataDiv" class="control-group">
		<label for="impegnabileCapitoloEntrata" class="control-label">Accertabile</label>
		<div class="controls">
			<input id="impegnabileCapitoloEntrata" type="checkbox" value="true"
				name="impegnabile" />
		</div>
	</div>

	<div class="fieldset">
		<div>
			<h4>Altri dati</h4>
		</div>
		<div>
			<div id="tipoFinanziamentoCapitoloEntrataDiv" class="control-group">
				<label for="tipoFinanziamentoCapitoloEntrata" class="control-label">Tipo
					Finanziamento</label>
				<div class="controls">
					<select id="tipoFinanziamentoCapitoloEntrata" class="span10"
						name="tipoFinanziamento.uid"></select>
				</div>
			</div>
			<div id="rilevanteIvaCapitoloEntrataDiv" class="control-group">
				<label for="rilevanteIvaCapitoloEntrata" class="control-label">Rilevante
					IVA</label>
				<div class="controls">
					<input id="rilevanteIvaCapitoloEntrata" type="checkbox"
						value="true" name="flagRilevanteIva" />
				</div>
			</div>
			<div id="tipoFondoCapitoloEntrataDiv" class="control-group">
				<label for="tipoFondoCapitoloEntrata" class="control-label">Tipo
					Fondo</label>
				<div class="controls">
					<select id="tipoFondoCapitoloEntrata" class="span10"
						name="tipoFondo.uid"></select>
				</div>
			</div>
			<div id="ricorrenteEntrataCapitoloEntrataDiv" class="control-group">
				<span class="control-label">Entrata</span>
				<div class="controls">
					<label class="radio inline"> <input type="radio"
						name="ricorrenteEntrata.uid" value="" checked="checked">&nbsp;Non
						si applica
					</label>&nbsp; <span id="ricorrenteEntrataCapitoloEntrata"></span>
				</div>
			</div>
			<div id="perimetroSanitarioEntrataCapitoloEntrataDiv"
				class="control-group">
				<label for="perimetroSanitarioEntrataCapitoloEntrata"
					class="control-label">Codifica identificativo del perimetro
					sanitario</label>
				<div class="controls">
					<select id="perimetroSanitarioEntrataCapitoloEntrata"
						class="span10" name="perimetroSanitarioEntrata.uid"></select>
				</div>
			</div>
			<div id="transazioneUnioneEuropeaEntrataCapitoloEntrataDiv"
				class="control-group">
				<label for="transazioneUnioneEuropeaEntrataCapitoloEntrata"
					class="control-label">Codifica transazione UE</label>
				<div class="controls">
					<select id="transazioneUnioneEuropeaEntrataCapitoloEntrata"
						class="span10" name="transazioneUnioneEuropeaEntrata.uid"></select>
				</div>
			</div>
			<s:iterator var="idx" begin="1"
				end="%{specificaCodifiche.numeroClassificatoriGenericiEntrata}">
				<div
					id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrataDiv"
					class="control-group">
					<label
						for="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrata"
						class="control-label"
						id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrataLabel"></label>
					<div class="controls">
						<select
							id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrata"
							class="span10"
							name="classificatoreGenerico<s:property value="%{#idx}" />.uid"></select>
					</div>
				</div>
			</s:iterator>
			<div id="noteCapitoloEntrataDiv" class="control-group">
				<label for="noteCapitoloEntrata" class="control-label">Note</label>
				<div class="controls">
					<textarea rows="5" cols="15" id="noteCapitoloEntrata" name="note"
						class="span10"></textarea>
				</div>
			</div> 
		</div>
	</div>--%>
</fieldset>
