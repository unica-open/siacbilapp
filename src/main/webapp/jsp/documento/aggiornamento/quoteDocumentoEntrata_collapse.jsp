<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<fieldset id="fieldsetInserisciQuota" class="form-horizontal">
		<h4 class="titleTxt"><span id="titoloSubdocumento"></span></h4>
		<h4 class="step-pane">Dati principali</h4>
		<fieldset class="form-horizontal">
			<s:hidden name="flagConvalidaManualeQuota"/>
			<s:if test="!datiIvaAccessibileQuota">
				<s:hidden name="subdocumento.flagRilevanteIVA" />
				<s:hidden name="subdocumento.numeroRegistrazioneIVA" />
			</s:if>
			
			<div class="control-group">
				<label class="control-label" for="numeroSubdocumento">Numero *</label>
				<div class="controls">
					<s:textfield id="numeroSubdocumento" placeholder="numero" cssClass="lbTextSmall span2 soloNumeri" required="true" disabled="true" value="%{progressivoNumeroSubdocumento}"  />
					<s:hidden name="subdocumento.numero" value="%{progressivoNumeroSubdocumento}" />
					<s:hidden id="uidSubdocumento" value="%{subdocumento.uid}" />
					<span class="alRight">
						<label class="radio inline" for="importoSubdocumento">Importo *</label>
					</span>
					<s:textfield id="importoSubdocumento" name="subdocumento.importo" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="importo" required="true" readonly="%{importoQuotaDisabilitato}" />
					<s:hidden name="subdocumento.importoDaDedurre" value="%{subdocumento.importoDaDedurre}" />
					<span class="alRight">
						<label class="radio inline" for="dataScadenzaSubdocumento">Data scadenza </label>
					</span>
					<s:textfield id="dataScadenzaSubdocumento" name="subdocumento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" required="true" maxlength="10" />
					<s:hidden name="subdocumento.uid" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="descrizioneSubdocumento">Descrizione *</label>
				<div class="controls">
					<s:textarea id="descrizioneSubdocumento" name="subdocumento.descrizione" cssClass="span10" rows="1" cols="15" placeholder="descrizione" required="true"></s:textarea>
				</div>
			</div>
			<div id="divSediSecondarie" class="step-pane active">
				<div class="accordion">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a href="#collapseSediSecondarie" data-parent="#divSediSecondarie" data-toggle="collapse" class="accordion-toggle collapsed">
								Sedi secondarie <span id="SPAN_sediSecondarieSoggetto"></span><span class="icon">&nbsp;</span>
							</a>
						</div>
						<div class="accordion-body collapse" id="collapseSediSecondarie">
							<s:include value="/jsp/soggetto/accordionSedeSecondariaSoggetto.jsp" />
						</div>
					</div>
				</div>
			</div>
		</fieldset>
		
		<h4 class="step-pane">Accertamento<span id="SPAN_accertamentoH4"></span></h4>
		<fieldset class="form-horizontal">
			<div class="control-group">
				<s:hidden name="forzaDisponibilitaAccertamento" id="forzaDisponibilitaAccertamento"/>
				<label class="control-label" for="annoMovimentoMovimentoGestione">Movimento</label>
				<div class="controls">
					<s:textfield id="annoMovimentoMovimentoGestione" name="movimentoGestione.annoMovimento" placeholder="anno" cssClass="span1 soloNumeri" maxlength="4" readonly="%{impegnoQuotaDisabilitato}"/>
					<s:textfield id="numeroMovimentoGestione" name="movimentoGestione.numero" placeholder="numero" cssClass="span2 soloNumeri" value="%{movimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
					<s:textfield id="numeroSubMovimentoGestione" name="subMovimentoGestione.numero" placeholder="subaccertamento" cssClass="span2 soloNumeri" maxlength="7" value="%{subMovimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
					<span id="SPAN_pulsanteAperturaCompilazioneGuidataAccertamento"class="radio guidata <s:if test="%{impegnoQuotaDisabilitato}"> hide</s:if>">
						<a class="btn btn-primary" data-toggle="modal" id="pulsanteAperturaCompilazioneGuidataAccertamento">compilazione guidata</a>
					</span>
				</div>
			</div>
		</fieldset>
		
		<s:if test="enteAbilitatoInserimentoAutomaticoAccertamento()">
			<h4 class="step-pane">Capitolo per generazione accertamento automatico - 
				<span id="SPAN_InformazioniCapitolo_QUOTE">
					<s:if test='%{capitolo != null && (capitolo.annoCapitolo ne null && capitolo.annoCapitolo != "") && (capitolo.numeroCapitolo ne null && capitolo.numeroCapitolo != "") && (capitolo.numeroArticolo ne null && capitolo.numeroArticolo != "")}'>
						<s:property value="%{capitolo.annoCapitolo + ' / ' + capitolo.numeroCapitolo  + ' / ' + capitolo.numeroArticolo}" />
						<s:if test='%{capitolo.numeroUEB ne null && capitolo.numeroUEB != ""}'>
							<s:property value="%' / '+{capitolo.numeroUEB}" />
						</s:if>
						<s:property value="%{' - ' + capitolo.descrizione}" />
					</s:if>	
				</span>
			</h4>
			<fieldset class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="annoCapitolo_QUOTE">Anno</label>
					<div class="controls">
						<s:textfield id="annoCapitolo_QUOTE" cssClass="lbTextSmall span1 soloNumeri" name="capitolo.annoCapitolo" maxlength="4" readonly="%{capitoloQuotaDisabilitato}"/>
						<span class="al">
							<label class="radio inline" for="numeroCapitolo_QUOTE">Capitolo *</label>
						</span>
						<s:textfield id="numeroCapitolo_QUOTE" cssClass="lbTextSmall span2 soloNumeri" name="capitolo.numeroCapitolo" readonly="%{capitoloQuotaDisabilitato}"/>
						<span class="al">
							<label class="radio inline" for="numeroArticolo_QUOTE">Articolo *</label>
						</span>
						<s:textfield id="numeroArticolo_QUOTE" cssClass="lbTextSmall span2 soloNumeri" name="capitolo.numeroArticolo" readonly="%{capitoloQuotaDisabilitato}"/>				
						<span class="al">
							<label class="radio inline" for="numeroUEB_QUOTE">UEB</label>
						</span>
						<s:textfield id="numeroUEB_QUOTE" cssClass="lbTextSmall span2 soloNumeri" name="capitolo.numeroUEB" readonly="%{capitoloQuotaDisabilitato}"/>
						<span id="SPAN_pulsanteApriModaleCapitolo" class="radio guidata <s:if test="%{capitoloQuotaDisabilitato}"> hide</s:if>">
							<a href="#" id="pulsanteApriModaleCapitolo" class="btn btn-primary">compilazione guidata</a>
						</span>
					</div>
				</div>
			</fieldset>
		</s:if>
		
		<s:if test="datiIvaAccessibileQuota">
			<div id="divIva">
				<h4 class="step-pane">Dati Iva</h4>
				<fieldset class="form-horizontal">
					<div class="control-group">
						<label class="control-label">Iva :</label>
						<div class="controls">
							<span class="alRight">
								<label for="flagRilevanteIvaSubdocumento" class="radio inline">rilevante </label>
							</span>
								<s:checkbox id="flagRilevanteIvaSubdocumento" name="subdocumento.flagRilevanteIVA" />
							<span class="alRight">
								<label for="numeroRegistrazioneIVASubdocumento" class="radio inline">Numero registrazione</label>
							</span>
							<s:textfield id="numeroRegistrazioneIVASubdocumento" name="subdocumento.numeroRegistrazioneIVA" placeholder="numero" cssClass="lbTextSmall span2 soloNumeri" />
							<span class="alRight <s:if test="%{!capitoloRilevanteIvaVisibile}"> hide</s:if>" id="spanCapitoloRilevanteIvaQuote">
								<div class="radio inline collapse_alert">
									<span class="icon-warning-sign icon-red alRight">&nbsp;</span>CAPITOLO RILEVANTE IVA
								</div>
							</span>
						</div>
					</div>
				</fieldset>
			</div>
		</s:if>
		
		<h4 class="step-pane">Provvedimento
			<span id="SPAN_InformazioniProvvedimento_QUOTE">
				<s:if test="%{attoAmministrativo != null && attoAmministrativo.anno != 0 && attoAmministrativo.numero != 0 && tipoAtto.uid != 0}">
					: <s:property value="attoAmministrativo.anno" />
					/ <s:property value="attoAmministrativo.numero" />
					- <s:property value="tipoAtto.codice" />
					- <s:property value="attoAmministrativo.oggetto" />
					<s:if test="%{strutturaAmministrativoContabile.uid != 0}">
						- <s:property value="strutturaAmministrativoContabile.codice" />-<s:property value="strutturaAmministrativoContabile.descrizione" />
					</s:if>
					- Stato: <s:property value="attoAmministrativo.statoOperativo" />
				</s:if>
			</span>
		</h4>
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="annoProvvedimento_QUOTE">Anno</label>
				<div class="controls">
					<s:textfield id="annoProvvedimento_QUOTE" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" maxlength="4" readonly="%{provvedimentoQuotaDisabilitato}"/>
					<span class="al">
						<label class="radio inline" for="numeroProvvedimento_QUOTE">Numero</label>
					</span>
					<s:textfield id="numeroProvvedimento_QUOTE" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" readonly="%{provvedimentoQuotaDisabilitato}"/>
					<span class="al">
						<label class="radio inline" for="tipoAttoProvvedimento_QUOTE">Tipo</label>
					</span>
					<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid" id="tipoAttoProvvedimento_QUOTE" cssClass="span4" headerKey="0" headerValue="" readonly="%{provvedimentoQuotaDisabilitato}"/>
					<s:hidden id="HIDDEN_statoProvvedimento_QUOTE" name="attoAmministrativo.statoOperativo" />
					<s:hidden id="HIDDEN_proseguireConElaborazioneAttoAmministrativo" name="proseguireConElaborazioneAttoAmministrativo" />
					
					<span class="radio guidata <s:if test="%{provvedimentoQuotaDisabilitato}"> hide</s:if>">
						<a href="#" id="pulsanteApriModaleProvvedimento_QUOTE" class="btn btn-primary">compilazione guidata</a>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Struttura Amministrativa</label>
				<div class="controls">
					<div class="accordion span8 struttAmm">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa_QUOTE" href="#struttAmm_QUOTE">
									<span id="SPAN_StrutturaAmministrativoContabile_QUOTE">Seleziona la Struttura amministrativa</span>
									<i class="icon-spin icon-refresh spinner"></i>
								</a>
							</div>
							<div id="struttAmm_QUOTE" class="accordion-body collapse">
								<div class="accordion-inner">
									<ul id="treeStruttAmm_QUOTE" class="ztree treeStruttAmm"></ul>
								</div>
							</div>
						</div>
					</div>
											
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_QUOTEUid" name="strutturaAmministrativoContabile.uid" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_QUOTECodice" name="strutturaAmministrativoContabile.codice" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_QUOTEDescrizione" name="strutturaAmministrativoContabile.descrizione" />
				</div>
			</div>
		</fieldset> 
			
		<br>
		<div id="accordionAltriDatiSubdocumento" class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#collapseAltriDatiSubdocumento" data-parent="#accordionAltriDatiSubdocumento" data-toggle="collapse" class="accordion-toggle">
						Altri dati<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div class="accordion-body collapse in" id="collapseAltriDatiSubdocumento">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">
							<div class="control-group">
								<label class="control-label">Provvisorio di Cassa</label>
								<div class="controls">
									<span class="alRight">
										<label for="annoProvvisorioCassaSubdocumento" class="radio inline">Anno</label>
									</span>
									<s:textfield id="annoProvvisorioCassaSubdocumento" name="subdocumento.provvisorioCassa.anno" cssClass="lbTextSmall span2 soloNumeri" placeholder="numero" maxlength="4" />
									<span class="alRight">
										<label for="numeroProvvisorioCassaSubdocumento" class="radio inline">Numero</label>
									</span>
									<s:textfield id="numeroProvvisorioCassaSubdocumento" name="subdocumento.provvisorioCassa.numero" cssClass="lbTextSmall span2 soloNumeri" placeholder="numero" maxlength="8" />
									&nbsp;
									<span id="causaleProvvisorioCassaSubdocumento"></span>
									<span class="radio guidata">
										<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento">compilazione guidata</button>
									</span>
								</div>
							</div>
							<div class="control-group">
								<label for="tipoAvvisoSubdocumento" class="control-label">Avviso</label>
								<div class="controls">
									<s:select list="listaTipoAvviso" name="tipoAvviso.uid" id="tipoAvvisoSubdocumento" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									<label for="flagOrdinativoSingoloSubdocumento" class="radio inline">Ordinativo singolo</label>
									<s:checkbox id="flagOrdinativoSingoloSubdocumento" name="subdocumento.flagOrdinativoSingolo" />
									<label for="flagEsproprioSubdocumento" class="radio inline">Esproprio</label>
									<s:checkbox id="flagEsproprioSubdocumento" name="subdocumento.flagEsproprio" />
									<!--<span class="alRight<s:if test="%{!etichettaAvvisoVisibile}"> hide</s:if>"> -->
									<span class="alRight">
										<div class="radio inline collapse_alert">
											<span class="icon-warning-sign icon-red alRight">&nbsp;</span> AVVISO: <s:if test="%{soggetto.avviso}"> SI </s:if><s:else> NO </s:else>
										</div>
									</span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="contoTesoreriaSubdocumento">Conto del tesoriere
								</label>
								<div class="controls input-append">
									<s:select list="listaContoTesoreria" name="contoTesoreria.uid" id="contoTesoreriaSubdocumento" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span9" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="noteTesoriereSubdocumento">Note al tesoriere
								<a href="#" class="tooltip-test" data-original-title="Selezionando un elemento dalla lista viene valorizzato il campo ‘Note al tesoriere’ in automatico, campo Note che è comunque editabile.">
										<i class="icon-info-sign">&nbsp;
											<span class="nascosto">Selezionando un elemento dalla lista viene valorizzato il campo ‘Note al tesoriere’ in automatico, campo Note che è comunque editabile.
											</span>
										</i>
								</a>
								</label>
								<div class="controls input-append">
									<s:select list="listaNoteTesoriere" name="noteTesoriere.uid" id="noteTesoriereSubdocumento" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span9" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="noteSubdocumento">Note</label>
								<div class="controls">
									<s:textarea id="noteSubdocumento" name="subdocumento.note" cssClass="span10" rows="3" cols="15"></s:textarea>
								</div>
							</div>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
		<input type = "hidden" name = "proseguireConElaborazione" value = "false"/>
		<input type = "hidden" name = "proseguireConElaborazioneAttoAmministrativo" value = "false"/>
	</fieldset>
	
	<p>
		<button type="button" class="btn btn-secondary" id="pulsanteAnnullaInserisciSubdocumento">annulla</button>
		<button type="button" class="btn btn-primary pull-right" id="pulsanteSalvaInserisciSubdocumento">	salva&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteQuotaSalva"></i>
		</button>
	</p>
</div>
<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp" />