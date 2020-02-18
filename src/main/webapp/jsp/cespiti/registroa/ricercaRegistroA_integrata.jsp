<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div data-pn="INT" class="hide">
	<div class="control-group">
		<label class="control-label"></label>
		<div class="controls">
			<label class="radio inline">
				Entrate <input type="radio" name="tipoElenco" value="E" <s:if test='%{"E".equals(tipoElenco)}'>checked</s:if> />
			</label>
			<span class="al">
				<label class="radio inline">
					Spese <input type="radio" name="tipoElenco" value="S" <s:if test='%{"S".equals(tipoElenco)}'>checked</s:if> />
				</label>
			</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="INT_tipoEvento">Tipo evento</label>
		<div class="controls">
			<s:select name="tipoEvento.uid" id="INT_tipoEvento" cssClass="span6" list="listaTipoEvento" headerKey="" headerValue="" listKey="uid" listValue="{codice + ' - ' + descrizione}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Numero</label>
		<div class="controls">
			<span class="al">
				<label class="radio inline" for="INT_primaNotaNumero">Provvisorio</label>
			</span>
			<s:textfield id="INT_primaNotaNumero" name="primaNota.numero" cssClass="span2 soloNumeri" />
			<span class="al">
				<label class="radio inline" for="INT_primaNotaNumeroRegistrazioneLibroGiornale">Definitivo</label>
			</span>
			<s:textfield id="INT_primaNotaNumeroRegistrazioneLibroGiornale" name="primaNota.numeroRegistrazioneLibroGiornale" cssClass="span2 soloNumeri" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="INT_contoCodice">Conto</label>
		<div class="controls">
			<s:textfield id="INT_contoCodice" name="conto.codice" cssClass="span3" />
			<span id="INT_descrizioneConto"></span>
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="INT_pulsanteCompilazioneGuidataConto">compilazione guidata</button>
			</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">
			Movimento finanziario&nbsp;
			<a href="#" class="tooltip-test" data-original-title="selezionare il Tipo evento">
				<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento</span></i>
			</a>
		</label>
		<div class="controls">
			<s:textfield id="INT_annoMovimento" cssClass="span1 soloNumeri" name="annoMovimento" maxlength="4" placeholder="anno" disabled="true" />
			<s:textfield id="INT_numeroMovimento" cssClass="span2" name="numeroMovimento" placeholder="numero" disabled="true" />
			<s:textfield id="INT_numeroSubmovimento" cssClass="span2" name="numeroSubmovimento" placeholder="sub" disabled="true" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="INT_primaNotaStatoOperativoPrimaNota">Stato Coge</label>
		<div class="controls">
			<s:select list="listaStatoOperativoPrimaNota" id="INT_primaNotaStatoOperativoPrimaNota" name="primaNota.statoOperativoPrimaNota"
				cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
		</div>
	</div>
	
	<div class="control-group"> 
		<label class="control-label" for="LIB_primaNotaStatoAccettazionePrimaNotaDefinitiva">Stato Inv</label>
		<div class="controls">
			<s:select name="primaNota.statoAccettazionePrimaNotaDefinitiva" id="LIB_primaNotaStatoAccettazionePrimaNotaDefinitiva" cssClass="span6" data-causale-ep="" list="listaStatoAccettazionePrimaNotaDefinitiva"
				headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
		</div>
	</div>
	
	
	<div class="control-group">
		<label class="control-label" for="INT_registrazioneMovFinElementoPianoDeiContiAggiornatoConto">Codice Conto Finanziario</label>
		<div class="controls">
			<s:textfield id="INT_registrazioneMovFinElementoPianoDeiContiAggiornatoConto" cssClass="span6" name="registrazioneMovFin.elementoPianoDeiContiAggiornato.codice"/>
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="INT_pulsanteCompilazioneGuidataContoFinanziario">compilazione guidata</button>
			</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="INT_primaNotaDescrizione">Descrizione</label>
		<div class="controls">
			<s:textfield id="INT_primaNotaDescrizione" name="primaNota.descrizione" cssClass="span9" />
		</div>
	</div>
	<div class="control-group hide" id="INT_containerImportoDocumento">
		<label class="control-label">
			Importo
			<a href="#" class="tooltip-test" data-html="true" data-original-title="selezionare il Tipo evento<br/>La ricerca sar&agrave; effettuata sull'importo del documento, non della prima nota">
				<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento<br/>La ricerca sar&agrave; effettuata sull'importo del documento, non della prima nota</span></i>
			</a>
		</label>
		<div class="controls">
			<span class="al">
				<label class="radio inline" for="INT_importoDocumentoDa">Da</label>
			</span>
			<s:textfield id="INT_importoDocumentoDa" cssClass="span2 soloNumeri decimale" name="importoDocumentoDa"  placeholder="importo da" disabled="true" />
			<span class="al">
				<label class="radio inline" for="INT_importoDocumentoA">A</label>
			</span>
			<s:textfield id="INT_importoDocumentoA" cssClass="span2 soloNumeri decimale" name="importoDocumentoA" placeholder="importo a" disabled="true" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Data registrazione definitiva</label>
		<div class="controls">
			<span class="al">
				<label class="radio inline" for="INT_dataRegistrazioneDa">Da</label>
			</span>
			<s:textfield id="INT_dataRegistrazioneDa" name="dataRegistrazioneDefinitivaDa" cssClass="span2 datepicker" maxlength="10" />
			<span class="al">
				<label class="radio inline" for="INT_dataRegistrazioneA">A</label>
			</span>
			<s:textfield id="INT_dataRegistrazioneA" name="dataRegistrazioneDefinitivaA" cssClass="span2 datepicker" maxlength="10" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Data registrazione provvisoria</label>
		<div class="controls">
			<span class="al">
				<label class="radio inline" for="INT_dataRegistrazioneProvvisoriaDa">Da</label>
			</span>
			<s:textfield id="INT_dataRegistrazioneProvvisoriaDa" name="dataRegistrazioneProvvisoriaDa" cssClass="span2 datepicker" maxlength="10" />
			<span class="al">
				<label class="radio inline" for="INT_dataRegistrazioneProvvisoriaA">A</label>
			</span>
			<s:textfield id="INT_dataRegistrazioneProvvisoriaA" name="dataRegistrazioneProvvisoriaA" cssClass="span2 datepicker" maxlength="10" />
		</div>
	</div>
	<h4 class="step-pane">Soggetto
		<a href="#" class="tooltip-test" data-original-title="selezionare il Tipo evento">
			<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento</span></i>
		</a>
		<span id="INT_descrizioneCompletaSoggetto"><s:property value="descrizioneCompletaSoggetto" /></span>
	</h4>
	<div class="control-group">
		<label class="control-label" for="codiceSoggetto">Codice </label>
		<div class="controls">
			<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="true" disabled="true" />
			<span class="radio guidata">
				<a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary hide">compilazione guidata</a>
			</span>
		</div>
	</div>
	<div id="provvedimentoContainer">
		<h4 class="step-pane">&nbsp;Provvedimento&nbsp;
			<a href="#" class="tooltip-test" data-original-title="selezionare il Tipo evento">
				<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento</span></i>
			</a>
			<span id="SPAN_InformazioniProvvedimento"><s:property value="descrizioneCompletaAttoAmministrativo" /></span>
		</h4>
		<div class="control-group">
			<label class="control-label" for="annoProvvedimento">Anno</label>
			<div class="controls">
				<s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" disabled="true" maxlength="4" />
				<span class="al">
					<label class="radio inline" for="numeroProvvedimento">Numero</label>
				</span>
				<s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" disabled="true" maxlength="7" />
				<span class="al">
					<label class="radio inline" for="tipoAttoProvvedimento">Tipo</label>
				</span>
				<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="attoAmministrativo.tipoAtto.uid" id="tipoAttoProvvedimento" cssClass="span4"
					headerKey="" headerValue="" disabled="true" />
				<s:hidden id="HIDDEN_statoProvvedimento" name="attoAmministrativo.statoOperativo" />
				<span class="radio guidata">
					<a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary hide">compilazione guidata</a>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Struttura Amministrativa</label>
			<div class="controls">
				<div class="accordion span8 struttAmm">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#struttAmm">
								<span id="SPAN_StrutturaAmministrativoContabile"> Seleziona la Struttura amministrativa </span>
								<i class="icon-spin icon-refresh spinner"></i>
							</a>
						</div>
						<div id="struttAmm" class="accordion-body collapse">
							<div class="accordion-inner">
								<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
								<button type="button" id="pulsanteDelesezionaStrutturaAmministrativoContabile" class="btn">Deseleziona</button>
							</div>
						</div>
					</div>
				</div>

				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="attoAmministrativo.strutturaAmmContabile.uid" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="attoAmministrativo.strutturaAmmContabile.codice" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="attoAmministrativo.strutturaAmmContabile.descrizione" />
			</div>
		</div>
	</div>
	<div id="capitoloContainer" class="hide">
		<h4 class="step-pane"> Dati capitolo &nbsp;
			<a href="#" class="tooltip-test" data-original-title="selezionare il Tipo evento">
				<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento</span></i>
			</a>
			<span class="datiRIFCapitolo" id="datiRiferimentoCapitoloSpan"></span>
		</h4>

		<div class="control-group">
			<label class="control-label">Capitolo</label>
			<div class="controls">
				<span class="al">
					<label class="radio inline" for="annoCapitolo">Anno</label>
				</span>
				<s:textfield id="annoCapitolo" name="capitolo.annoCapitolo"
					cssClass="lbTextSmall span1" value="%{annoEsercizioInt}" disabled="true" data-maintain="" />
				<s:hidden name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" data-maintain="" />
				<span class="al">
					<label class="radio inline" for="numeroCapitolo">Capitolo</label>
				</span>
				<s:textfield id="numeroCapitolo" name="capitolo.numeroCapitolo" cssClass="lbTextSmall span2 soloNumeri" maxlength="30" />
				<span class="al">
					<label class="radio inline" for="numeroArticolo">Articolo</label>
				</span>
				<s:textfield id="numeroArticolo" name="capitolo.numeroArticolo" cssClass="lbTextSmall span2 soloNumeri" maxlength="7" />
				<s:if test="gestioneUEB">
					<span class="al">
						<label class="radio inline" for="numeroUEB">UEB</label>
					</span>
					<s:textfield id="numeroUEB" name="capitolo.numeroUEB" cssClass="lbTextSmall span2 soloNumeri" maxlength="7"/>
				</s:if><s:else>
					<input type="hidden" name="capitolo.numeroUEB" value="1" data-maintain="true" />
				</s:else>
				<span class="radio guidata" id="compilazioneGuidata">
					<a class="btn btn-primary" data-toggle="modal" id="pulsanteApriCompilazioneGuidataCapitolo">
						compilazione guidata
					</a>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Struttura Amministrativa</label>
			<div class="controls">
				<div class="accordion span8 struttAmm">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#struttAmmCap">
								<span id="SPAN_StrutturaAmministrativoContabileCap"> Seleziona la Struttura amministrativa </span>
								<i class="icon-spin icon-refresh spinner"></i>
							</a>
						</div>
						<div id="struttAmmCap" class="accordion-body collapse">
							<div class="accordion-inner">
								<ul id="treeStruttAmmCap" class="ztree treeStruttAmmCap"></ul>
								<button type="button" class="btn" data-uncheck-ztree="treeStruttAmmCap">Deseleziona</button>
							</div>
						</div>
					</div>
				</div>

				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCapUid" name="strutturaAmministrativoContabile.uid" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCapCodice" name="strutturaAmministrativoContabile.codice" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCapDescrizione" name="strutturaAmministrativoContabile.descrizione" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCapStringa" name="stringaSACCapitolo" />

			</div>
		</div>
	</div>

	<div id="containerImpegno" class="hide">
		<h4 class="step-pane">Impegno<span id="SPAN_impegnoH4"></span></h4>
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="annoMovimentoMovimentoGestione">Movimento</label>
				<div class="controls">
					<s:textfield id="annoImpegno" name="impegno.annoMovimento" placeholder="anno" cssClass="span1 soloNumeri" maxlength="4"/>
					<s:textfield id="numeroImpegno" name="impegno.numero" placeholder="numero" cssClass="span2 soloNumeri"/>
					<s:textfield id="numeroSubimpegno" name="subImpegno.numero" placeholder="sub" cssClass="span2 soloNumeri" maxlength="7"/>
					<span id="SPAN_pulsanteAperturaCompilazioneGuidataImpegno"class="radio guidata">
						<a class="btn btn-primary" data-toggle="modal" id="pulsanteAperturaCompilazioneGuidataImpegno">compilazione guidata</a>
					</span>
				</div>
			</div>
		</fieldset>
	</div>

	<div id="containerAccertamento" class="hide">
		<h4 class="step-pane">Accertamento<span id="SPAN_accertamentoH4"></span></h4>
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="annoMovimentoMovimentoGestione">Movimento</label>
				<div class="controls">
					<s:textfield id="annoAccertamento" name="accertamento.annoMovimento" placeholder="anno" cssClass="span1 soloNumeri" maxlength="4" readonly="%{impegnoQuotaDisabilitato}"/>
					<s:textfield id="numeroAccertamento" name="accertamento.numero" placeholder="numero" cssClass="span2 soloNumeri" value="%{movimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
					<s:textfield id="numeroSubAccertamento" name="subAccertamento.numero" placeholder="subccertamento" cssClass="span2 soloNumeri" maxlength="7" value="%{subMovimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
					<span id="SPAN_pulsanteAperturaCompilazioneGuidataAccertamento"class="radio guidata">
						<a class="btn btn-primary" data-toggle="modal" id="pulsanteAperturaCompilazioneGuidataAccertamento">compilazione guidata</a>
					</span>
				</div>
			</div>
		</fieldset>
	</div>
</div>