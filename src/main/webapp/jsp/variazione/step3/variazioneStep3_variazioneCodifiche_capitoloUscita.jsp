<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset id="fieldsetCapitoloUscita" class="form-horizontal hide">
	<s:hidden name="uid" id="HIDDEN_uidElementoCapitoloCodificheUscita" />
	<div id="descrizioneCapitoloCapitoloUscitaDiv" class="control-group">
		<label for="descrizioneCapitoloCapitoloUscita" class="control-label">Descrizione
			*</label>
		<div class="controls">
			<textarea rows="5" cols="15" id="descrizioneCapitoloCapitoloUscita"
				class="span10" name="descrizioneCapitolo"></textarea>
		</div>
	</div>
	<div id="descrizioneArticoloCapitoloUscitaDiv" class="control-group">
		<label for="descrizioneArticoloCapitoloUscita" class="control-label">Descrizione
			Articolo</label>
		<div class="controls">
			<textarea rows="5" cols="15" id="descrizioneArticoloCapitoloUscita"
				class="span10" name="descrizioneArticolo"></textarea>
		</div>
	</div> 
	<%-- <div id="missioneCapitoloUscitaDiv" class="control-group">
		<label for="missioneCapitoloUscita" class="control-label">Missione
			*</label>
		<div class="controls">
			<select id="missioneCapitoloUscita" class="span10"
				name="missione.uid"></select>
		</div>
	</div>
	<div id="programmaCapitoloUscitaDiv" class="control-group">
		<label for="programmaCapitoloUscita" class="control-label">
			Programma * <a class="tooltip-test"
			title="selezionare prima la Missione" href="#"><i
				class="icon-info-sign"></i></a>
		</label>
		<div class="controls">
			<select id="programmaCapitoloUscita" class="span10"
				name="programma.uid"></select>
		</div>
	</div>
	<div id="classificazioneCofogCapitoloUscitaDiv" class="control-group">
		<label for="classificazioneCofogCapitoloUscita" class="control-label">
			Cofog * <a class="tooltip-test"
			title="selezionare prima il Programma" href="#"><i
				class="icon-info-sign"></i></a>
		</label>
		<div class="controls">
			<select id="classificazioneCofogCapitoloUscita" class="span10"
				name="classificazioneCofog.uid"></select>
		</div>
	</div>
	<div id="titoloSpesaCapitoloUscitaDiv" class="control-group">
		<label for="titoloSpesaCapitoloUscita" class="control-label">Titolo
			*</label>
		<div class="controls">
			<select id="titoloSpesaCapitoloUscita" class="span10"
				name="titoloSpesa.uid"></select>
		</div>
	</div>
	<div id="macroaggregatoCapitoloUscitaDiv" class="control-group">
		<label for="macroaggregatoCapitoloUscita" class="control-label">
			Macroaggregato * <a class="tooltip-test"
			title="selezionare prima il Titolo" href="#"><i
				class="icon-info-sign"></i></a>
		</label>
		<div class="controls">
			<select id="macroaggregatoCapitoloUscita" class="span10"
				name="macroaggregato.uid"></select>
		</div>
	</div>
	<div id="elementoPianoDeiContiCapitoloUscitaDiv" class="control-group">
		<label for="elementoPianoDeiContiCapitoloUscita" class="control-label">
			<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario <a
			class="tooltip-test" title="selezionare prima il Macroaggregato"
			href="#"><i class="icon-info-sign"></i></a>
		</label>
		<div class="controls">
			<input type="hidden" id="elementoPianoDeiContiCapitoloUscita"
				name="elementoPianoDeiConti.uid" /> <a href="#" role="button"
				class="btn" data-toggle="modal"
				id="elementoPianoDeiContiCapitoloUscitaPulsante"> Seleziona il
				Piano dei Conti&nbsp; <i class="icon-spin icon-refresh spinner"
				id="elementoPianoDeiContiCapitoloUscitaSpinner"></i>
			</a>

			<div id="elementoPianoDeiContiCapitoloUscitaModale"
				class="modal hide fade" tabindex="-1" role="dialog"
				aria-labelledby="elementoPianoDeiContiCapitoloUscitaModalLabel"
				aria-hidden="true">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="elementoPianoDeiContiCapitoloUscitaModalLabel">Piano
						dei Conti</h3>
				</div>
				<div class="modal-body">
					<ul id="elementoPianoDeiContiCapitoloUscitaTree" class="ztree"></ul>
				</div>
				<div class="modal-footer">
					<button id="elementoPianoDeiContiCapitoloUscitaDeseleziona"
						class="btn">Deseleziona</button>
					<button type="button" class="btn btn-primary pull-right"
						data-dismiss="modal" aria-hidden="true">Conferma</button>
				</div>
			</div>
			&nbsp; <span id="elementoPianoDeiContiCapitoloUscitaSpan">
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
			<s:textfield id="siopeSpesa" data-ricerca-url="ricercaClassificatoreGerarchicoByCodice_siopeSpesa.do" name="siopeSpesa.codice"
				cssClass="span3" />
			&nbsp;<span id="descrizioneSiopeSpesa"><s:property
					value="siopeSpesa.descrizione" /></span>
			<s:hidden id="HIDDEN_idSiopeSpesa" name="siopeSpesa.uid" />
			<s:hidden id="HIDDEN_descrizioneSiopeSpesa"
				name="siopeSpesa.descrizione" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary"
					id="compilazioneGuidataSIOPESpesa">compilazione guidata</button>
			</span>
		</div>
	</div>
 --%>
	<%-- RIPRISTINARE --%>
	<!-- <input type="hidden" id="siopeSpesaCapitoloUscita" name="siopeSpesa.uid" /> -->

	<%--div id="siopeSpesaCapitoloUscitaDiv" class="control-group">
														<label for="siopeSpesaCapitoloUscita" class="control-label">
															<abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
															<a class="tooltip-test" title="selezionare prima il P.d.C." href="#"><i class="icon-info-sign"></i></a>
														</label>
														<div class="controls">
															<input type="hidden" id="siopeSpesaCapitoloUscita" name="siopeSpesa.uid" />
															<a href="#" role="button" class="btn" data-toggle="modal" id="siopeSpesaCapitoloUscitaPulsante">
																Seleziona il codice SIOPE&nbsp;
																<i class="icon-spin icon-refresh spinner" id="siopeSpesaCapitoloUscitaSpinner"></i>
															</a>
															<div id="siopeSpesaCapitoloUscitaModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="siopeSpesaCapitoloUscitaModalLabel" aria-hidden="true">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
																	<h3 id="siopeSpesaCapitoloUscitaModalLabel">SIOPE</h3>
																</div>
																<div class="modal-body">
																	<ul id="siopeSpesaCapitoloUscitaTree" class="ztree"></ul>
																</div>
																<div class="modal-footer">
																	<button id="siopeSpesaCapitoloUscitaDeseleziona" class="btn">Deseleziona</button>
																	<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
																</div>
															</div>
															&nbsp;
															<span id="siopeSpesaCapitoloUscitaSpan">
																	Nessun SIOPE selezionato
															</span>
														</div>
													</div--%>
	<div id="strutturaAmministrativoContabileCapitoloUscitaDiv"
		class="control-group">
		<label for="strutturaAmministrativoContabileCapitoloUscita"
			class="control-label"> Struttura Amministrativa Responsabile
		</label>
		<div class="controls">
			<input type="hidden"
				id="strutturaAmministrativoContabileCapitoloUscita"
				name="strutturaAmministrativoContabile.uid" /> <a href="#"
				role="button" class="btn" data-toggle="modal"
				id="strutturaAmministrativoContabileCapitoloUscitaPulsante">
				Seleziona la Struttura Amministrativa&nbsp; <i
				class="icon-spin icon-refresh spinner"
				id="strutturaAmministrativoContabileCapitoloUscitaSpinner"></i>
			</a>
			<div id="strutturaAmministrativoContabileCapitoloUscitaModale"
				class="modal hide fade" tabindex="-1" role="dialog"
				aria-labelledby="strutturaAmministrativoContabileCapitoloUscitaModalLabel"
				aria-hidden="true">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="strutturaAmministrativoContabileCapitoloUscitaModalLabel">Struttura
						Amministrativa Responsabile</h3>
				</div>
				<div class="modal-body">
					<ul id="strutturaAmministrativoContabileCapitoloUscitaTree"
						class="ztree"></ul>
				</div>
				<div class="modal-footer">
					<button
						id="strutturaAmministrativoContabileCapitoloUscitaDeseleziona"
						class="btn">Deseleziona</button>
					<button type="button" class="btn btn-primary pull-right"
						data-dismiss="modal" aria-hidden="true">Conferma</button>
				</div>
			</div>
			&nbsp; <span id="strutturaAmministrativoContabileCapitoloUscitaSpan">
				Nessuna Struttura Amministrativa Responsabile selezionata </span>
		</div>
	</div>
	<%-- <div id="categoriaCapitoloCapitoloUscitaDiv" class="control-group">
		<label for="categoriaCapitoloCapitoloUscita" class="control-label">Tipo
			Capitolo</label>
		<div class="controls">
			<select id="categoriaCapitoloCapitoloUscita" class="span10"
				name="categoriaCapitolo.uid"></select>
		</div>
	</div>
	<div id="impegnabileCapitoloUscitaDiv" class="control-group">
		<label for="impegnabileCapitoloUscita" class="control-label">Impegnabile</label>
		<div class="controls">
			<input id="impegnabileCapitoloUscita" type="checkbox" value="true"
				name="impegnabile" />
		</div>
	</div>

	<div class="fieldset">
		<div>
			<h4>Altri dati</h4>
		</div>
		<div>
			<div id="corsivoPerMemoriaCapitoloUscitaDiv" class="control-group">
				<label for="corsivoPerMemoriaCapitoloUscita" class="control-label">Corsivo
					per memoria</label>
				<div class="controls">
					<input id="corsivoPerMemoriaCapitoloUscita" type="checkbox"
						value="true" name="flagPerMemoria" />
				</div>
			</div>
			<div id="tipoFinanziamentoCapitoloUscitaDiv" class="control-group">
				<label for="tipoFinanziamentoCapitoloUscita" class="control-label">Tipo
					Finanziamento</label>
				<div class="controls">
					<select id="tipoFinanziamentoCapitoloUscita" class="span10"
						name="tipoFinanziamento.uid"></select>
				</div>
			</div>
			<div id="rilevanteIvaCapitoloUscitaeDiv" class="control-group">
				<label for="rilevanteIvaCapitoloUscita" class="control-label">Rilevante
					IVA</label>
				<div class="controls">
					<input id="rilevanteIvaCapitoloUscita" type="checkbox" value="true"
						name="flagRilevanteIva" />
				</div>
			</div>
			<div id="funzioniDelegateRegioneCapitoloUscitaDiv"
				class="control-group">
				<label for="funzioniDelegateRegioneCapitoloUscita"
					class="control-label">Funzioni delegate regione</label>
				<div class="controls">
					<input id="funzioniDelegateRegioneCapitoloUscita" type="checkbox"
						value="true" name="funzDelegateRegione" />
				</div>
			</div>
			<div id="tipoFondoCapitoloUscitaDiv" class="control-group">
				<label for="tipoFondoCapitoloUscita" class="control-label">Tipo
					Fondo</label>
				<div class="controls">
					<select id="tipoFondoCapitoloUscita" class="span10"
						name="tipoFondo.uid"></select>
				</div>
			</div>
			<div id="ricorrenteSpesaCapitoloUscitaDiv" class="control-group">
				<span class="control-label">Spesa</span>
				<div class="controls">
					<label class="radio inline"> <input type="radio"
						name="ricorrenteSpesa.uid" value="" checked="checked">&nbsp;Non
						si applica
					</label>&nbsp; <span id="ricorrenteSpesaCapitoloUscita"></span>
				</div>
			</div>
			<div id="perimetroSanitarioSpesaCapitoloUscitaDiv"
				class="control-group">
				<label for="perimetroSanitarioSpesaCapitoloUscita"
					class="control-label">Codifica identificativo del perimetro
					sanitario</label>
				<div class="controls">
					<select id="perimetroSanitarioSpesaCapitoloUscita" class="span10"
						name="perimetroSanitarioSpesa.uid"></select>
				</div>
			</div>
			<div id="transazioneUnioneEuropeaSpesaCapitoloUscitaDiv"
				class="control-group">
				<label for="transazioneUnioneEuropeaSpesaCapitoloUscita"
					class="control-label">Codifica transazione UE</label>
				<div class="controls">
					<select id="transazioneUnioneEuropeaSpesaCapitoloUscita"
						class="span10" name="transazioneUnioneEuropeaSpesa.uid"></select>
				</div>
			</div>
			<div id="politicheRegionaliUnitarieCapitoloUscitaDiv"
				class="control-group">
				<label for="politicheRegionaliUnitarieCapitoloUscita"
					class="control-label">Codifica politiche regionali unitarie</label>
				<div class="controls">
					<select id="politicheRegionaliUnitarieCapitoloUscita"
						class="span10" name="politicheRegionaliUnitarie.uid"></select>
				</div>
			</div>
			<s:iterator var="idx" begin="1"
				end="%{specificaCodifiche.numeroClassificatoriGenericiSpesa}">
				<div
					id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscitaDiv"
					class="control-group">
					<label
						for="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscita"
						class="control-label"
						id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscitaLabel"></label>
					<div class="controls">
						<select
							id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscita"
							class="span10"
							name="classificatoreGenerico<s:property value="%{#idx}" />.uid"></select>
					</div>
				</div>
			</s:iterator>
			<div id="noteCapitoloUscitaDiv" class="control-group">
				<label for="noteCapitoloUscita" class="control-label">Note</label>
				<div class="controls">
					<textarea rows="5" cols="15" id="noteCapitoloUscita" name="note"
						class="span10"></textarea>
				</div>
			</div>
		</div>
	</div>  --%>
	
	
	
	<%-- <s:include value="/jsp/variazione/step3/modaleCompilazioneGuidataSIOPEVariazione.jsp">
		<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeSpesa.do</s:param>
		<s:param name="ajaxUrl">risultatiRicercaSiopeSpesaAjax.do</s:param>
	</s:include> --%>
	<%-- <s:include value="/jsp/cap/modaleCompilazioneGuidataSIOPE.jsp">
		<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeSpesa.do</s:param>
		<s:param name="ajaxUrl">risultatiRicercaSiopeSpesaAjax.do</s:param>
	</s:include> --%>
</fieldset>
