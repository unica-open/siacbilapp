<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">

	<fieldset id="fieldsetInserisciQuota" class="form-horizontal">
		<s:hidden id="HIDDEN_datiSospensioneQuotaEditabili" name="datiSospensioneQuotaEditabili" />
		<h4 class="titleTxt"><span id="titoloSubdocumento"></span></h4>
		<h4 class="step-pane">Dati principali</h4>
		<fieldset class="form-horizontal">
			<s:hidden name="subdocumento.liquidazione.uid" />
			<s:hidden name="subdocumento.liquidazione.numeroLiquidazione" />
			<s:hidden name="subdocumento.liquidazione.annoLiquidazione" />
			<s:hidden name="subdocumento.liquidazione.statoOperativoLiquidazione" />
			<s:hidden name="flagConvalidaManualeQuota"/>
			<s:if test="!datiIvaAccessibileQuota">
				<s:hidden name="subdocumento.flagRilevanteIVA" />
				<s:hidden name="subdocumento.numeroRegistrazioneIVA" />
			</s:if>
			
			<div class="control-group">
				<label for="numeroSubdocumento" class="control-label">Numero *</label>
				<div class="controls">
					<s:textfield id="numeroSubdocumento" placeholder="numero" cssClass="lbTextSmall span2 soloNumeri" required="true" disabled="true" value="%{progressivoNumeroSubdocumento}" />
					<s:hidden name="subdocumento.numero" value="%{progressivoNumeroSubdocumento}" />
					<s:hidden name="subdocumento.uid" id="uidSubdocumento" />
					<span class="alRight">
						<label for="importoSubdocumento" class="radio inline">Importo *</label>
					</span>
						<s:textfield id="importoSubdocumento" name="subdocumento.importo" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="importo" required="true" readonly="%{importoQuotaDisabilitato}" />
						<s:hidden name="subdocumento.importoDaDedurre" value="%{subdocumento.importoDaDedurre}" />
				</div>
			</div>



			<div class="control-group">
				<label for="dataScadenzaSubdocumento" class="control-label">Data scadenza *</label>
				<div class="controls">
					<s:textfield id="dataScadenzaSubdocumento" name="subdocumento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" required="true" maxlength="10" />
					<s:hidden id="dataScadenzaOriginaleQuota" name="dataScadenzaOriginale" />
					<s:hidden id="dataScadenzaDopoSospensioneOriginaleQuota" name="dataScadenzaDopoSospensioneOriginale" />
					<s:if test="flagDatiSospensioneEditabili">
						<span class="alRight">
							<label for="dataScadenzaDopoSospensioneSubdocumento" class="radio inline">Data scadenza dopo sospensione&nbsp;
									<a href="#" class="tooltip-test" data-original-title="Per poter riattivare la quota è necessario effettuare la prima contabilizzazione.">
										<i class="icon-info-sign">&nbsp;
											<span class="nascosto">Per poter riattivare la quota è necessario effettuare la prima contabilizzazione.
											</span>
										</i>
									</a>
							
							</label>
						</span>
						
						<s:textfield id="dataScadenzaDopoSospensioneSubdocumento" disabled="!editDateFromPccActive" name="subdocumento.dataScadenzaDopoSospensione" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza dopo sospensione" maxlength="10" />
						<s:if test="!editDateFromPccActive">
							<s:hidden name="subdocumento.dataScadenzaDopoSospensione" />						
						</s:if>
					
					</s:if>
					
					
					<span class="alRight">
						<label for="dataEsecuzionePagamentoSubdocumento" class="radio inline">Data esecuzione pagamento</label>
					</span>
					<s:textfield id="dataEsecuzionePagamentoSubdocumento" name="subdocumento.dataEsecuzionePagamento" cssClass="lbTextSmall span2 datepicker" placeholder="data esecuzione" maxlength="10" />
				</div>
			</div>
			
			
			<div class="control-group">
				<label for="siopeScadenzaMotivo" class="control-label">Motivo scadenza siope</label>
				<div class="controls">
					<s:select list="listaSiopeScadenzaMotivo" id="siopeScadenzaMotivo" name="subdocumento.siopeScadenzaMotivo.uid" cssClass="span6"
						headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="false" />
				</div>
			</div>

			<div class="control-group">
				<label class="control-label" for="descrizioneSubdocumento">Descrizione *</label>
				<div class="controls">
					<s:textarea id="descrizioneSubdocumento" name="subdocumento.descrizione" cssClass="span10" rows="1" cols="15" placeholder="descrizione" required="true"></s:textarea>
				</div>
			</div>

			<div id="divDatiSospensioneQuota" class="step-pane active">
				<div class="accordion">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a href="#collapseDatiSospensioneQuota" data-parent="#divDatiSospensioneQuota" data-toggle="collapse" class="accordion-toggle collapsed">
								Dati sospensione <span class="icon">&nbsp;</span>
							</a>
						</div>
						<div class="accordion-body collapse" id="collapseDatiSospensioneQuota">
							<s:include value="/jsp/documento/aggiornamento/include/accordionDatiSospensioneQuota.jsp" />
						</div>
					</div>
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
			<div id="divModalitaPagamento" class="step-pane active">
				<div class="accordion">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a href="#collapseModalitaPagamento" data-parent="#divModalitaPagamento" data-toggle="collapse" class="accordion-toggle">
								 Modalit&agrave; pagamento <span class="datiPagamento" id="SPAN_modalitaPagamentoSoggetto"></span><span class="icon">&nbsp;</span></a>
						</div>
						<div class="accordion-body collapse in" id="collapseModalitaPagamento">
							<s:include value="/jsp/soggetto/accordionModalitaPagamentoSoggetto.jsp" />
						</div>
					</div>
				</div>
			</div>
		</fieldset>

		<h4 class="step-pane">Impegno<span id="SPAN_impegnoH4"></span></h4>
		<fieldset class="form-horizontal" id="fieldsetImpegnoQuota">
			<div class="control-group">
				<label class="control-label" for="annoMovimentoMovimentoGestione">Movimento</label>
				<div class="controls">
					<s:textfield id="annoMovimentoMovimentoGestione" name="movimentoGestione.annoMovimento" placeholder="anno" cssClass="span1 soloNumeri" maxlength="4" readonly="%{impegnoQuotaDisabilitato}"/>
					<s:textfield id="numeroMovimentoGestione" name="movimentoGestione.numero" placeholder="numero" cssClass="span2 soloNumeri" value="%{movimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
					<s:textfield id="numeroSubmovimento" name="subMovimentoGestione.numero" placeholder="subimpegno" cssClass="span2 soloNumeri" maxlength="7" value="%{subMovimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
					<span class="radio guidata <s:if test="%{impegnoQuotaDisabilitato}"> hide</s:if>">
						<a class="btn btn-primary" data-toggle="modal" id="pulsanteAperturaCompilazioneGuidataImpegno">compilazione guidata</a>
					</span>
					<%-- SIAC-7859 -- workaround -- non trovo "chi" allarga il fieldset pertanto ne forzo il comportamento --%>
					<s:if test="ingressoTabQuote">
						<s:hidden id="ITQ" value="true"></s:hidden>
					</s:if>
				</div>
			</div>
			
			<div class="control-group datiVisibili" data-cig-cup>
				<label class="control-label" for="cigMovimentoGestione">
					<abbr title="codice identificativo gara">CIG</abbr>
				</label>
				<div class="controls">
					<s:textfield id="cigMovimentoGestione" name="subdocumento.cig" cssClass="span3 forzaMaiuscole" data-allowed-chars="[A-Za-z0-9]" maxlength="10" readonly="%{impegnoQuotaDisabilitato}"/>
					<span class="al <s:if test="%{!showSiopeAssenzaMotivazione}"> hide </s:if>" data-assenza-cig>
						<label for="siopeAssenzaMotivazione" class="radio inline">Motivo di assenza CIG</label>
					</span>
					<s:if test="showSiopeAssenzaMotivazione">
						<s:select data-assenza-cig="" list="listaSiopeAssenzaMotivazione" cssClass="span4" id="siopeAssenzaMotivazione" name="subdocumento.siopeAssenzaMotivazione.uid"
							headerKey="0" headerValue="" listKey="uid" disabled ="" listValue="%{codice + ' - ' + descrizione}" required="false" disabled="impegnoQuotaDisabilitato" />
					</s:if><s:else>
						<s:select data-assenza-cig="" list="listaSiopeAssenzaMotivazione" cssClass="span4 hide" id="siopeAssenzaMotivazione" name="subdocumento.siopeAssenzaMotivazione.uid"
							headerKey="0" headerValue="" listKey="uid" disabled ="" listValue="%{codice + ' - ' + descrizione}" required="false" disabled="impegnoQuotaDisabilitato" />
					</s:else>
					<s:if test="impegnoQuotaDisabilitato">
						<s:hidden name="subdocumento.siopeAssenzaMotivazione.uid" />
					</s:if>
				</div>
			</div>
			<div class="control-group datiVisibili" data-cig-cup>
				<label class="control-label" for="cupMovimentoGestione">
					<abbr title="codice unico progetto">CUP</abbr>
				</label>
				<div class="controls">
					<s:textfield id="cupMovimentoGestione" name="subdocumento.cup" cssClass="span3 forzaMaiuscole" data-allowed-chars="[A-Za-z0-9]" maxlength="15" readonly="%{impegnoQuotaDisabilitato}"/>
				</div>
			</div>
			
		</fieldset>
		<%-- SIAC-8153 START --%>
		<h4 class="step-pane">Struttura competente
		<%--
			 <s:if test="strutturaCompetenteQuota != null">: <s:property value="%{strutturaCompetenteQuota.codice}"/> - <s:property value="%{strutturaCompetenteQuota.descrizione}"/></s:if>
		 --%>
		<span id="SPAN_impegnoH4"></span></h4>
		<fieldset class="form-horizontal" id="fieldsetStrutturaCompetenteQuota">
			<div class="control-group">
				<label class="control-label">Struttura competente alla quale attribuire la spesa</label>
				<div class="controls">
					<div class="accordion span8 struttAmm whitespace-nowrap">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa_QUOTA_SD" href="#struttAmm_QUOTE_SD">
									<span id="SPAN_StrutturaAmministrativoContabile_QUOTE_SD">Seleziona la Struttura competente</span>
								</a>
							</div>
							<div id="struttAmm_QUOTE_SD" class="accordion-body collapse">
								<div class="accordion-inner">
									<ul id="treeStruttAmm_QUOTE_SD" class="ztree treeStruttAmm"></ul>
									<%-- 
										<button type="button" id="pulsanteDelesezionaStrutturaAmministrativoContabile_QUOTE_SD" class="btn">Deseleziona</button>
									--%>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid_QUOTE_SD" name="strutturaCompetenteQuota.uid" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice_QUOTE_SD" name="strutturaCompetenteQuota.codice" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione_QUOTE_SD" name="strutturaCompetenteQuota.descrizione" />
		</fieldset>
		<%-- SIAC-8153 END --%>
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
							<s:textfield id="numeroRegistrazioneIVASubdocumento" name="subdocumento.numeroRegistrazioneIVA" placeholder="numero" cssClass="lbTextSmall span2 soloNumeri" readonly="true" />
							<span class="alRight <s:if test="%{!capitoloRilevanteIvaVisibile}">hide</s:if>" id="spanCapitoloRilevanteIvaQuote">
								<div class="radio inline collapse_alert">
									<span class="icon-warning-sign icon-red alRight">&nbsp;</span>CAPITOLO RILEVANTE IVA
								</div>
							</span>
						</div>
					</div>
				</fieldset>
			</div>
		</s:if>

		<div id="divIvaSR">
			<h4 class="step-pane">Dati Iva Split/Reverse</h4>
			<fieldset class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="tipoIvaSplitReverse_QUOTE">Tipo Iva Split/Reverse/Esente:</label>
					<div class="controls">
						<s:select list="listaTipoIvaSplitReverse"  listValue="%{codice + ' - ' + descrizione}" name="subdocumento.tipoIvaSplitReverse" 
							id="tipoIvaSplitReverse_QUOTE" cssClass="span4" headerKey="" headerValue=""/>
						<span class="alRight">
							<label for="importoSplitReverse" class="radio inline">Importo</label>
						</span>
						<s:textfield id="importoSplitReverse" name="subdocumento.importoSplitReverse"  cssClass="lbTextSmall span2 soloNumeri decimale"  />
					</div>
				</div>
			</fieldset>
		</div>
		
		<s:hidden id="azioneProvvedimentoConsentita" name="azioneProvvedimentoConsentita"/>
		<div id = "divProvvedimento">
		<s:hidden id="HIDDEN_proseguireConElaborazioneAttoAmministrativo" name="proseguireConElaborazioneAttoAmministrativo" />
		
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
						<label class="radio inline" for="tipoAtto_QUOTE">Tipo</label>
					</span>
					<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid" id="tipoAtto_QUOTE" cssClass="span4" headerKey="0" headerValue="" disabled="%{provvedimentoQuotaDisabilitato}"/>
					<s:if test="provvedimentoQuotaDisabilitato">
						<s:hidden name="tipoAtto.uid" id="tipoAtto_QUOTE_HIDDEN" />
					</s:if>
					<s:hidden id="HIDDEN_statoProvvedimento_QUOTE" name="attoAmministrativo.statoOperativo" />
					<span class="radio guidata <s:if test="%{provvedimentoQuotaDisabilitato}"> hide</s:if>">
						<a href="#" id="pulsanteApriModaleProvvedimento_QUOTE" class="btn btn-primary">compilazione guidata</a>
					</span>
					<span class="radio guidata <s:if test="%{provvedimentoQuotaDisabilitato}"> hide</s:if>">
						<a href="#" id="modaleInserimentoProvvedimento_pulsanteApertura" class="btn btn-primary">inserisci provvedimento</a>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Struttura Amministrativa</label>
				<div class="controls">
					<div class="accordion span8 struttAmm">
						<div class="accordion-group">
							<s:if test="%{!provvedimentoQuotaDisabilitato}">
								<div class="accordion-heading">
									<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa_QUOTE" href="#struttAmm_QUOTE">
										<span id="SPAN_StrutturaAmministrativoContabile_QUOTE">Seleziona la Struttura amministrativa</span>
									</a>
								</div>
								<div id="struttAmm_QUOTE" class="accordion-body collapse">
									<div class="accordion-inner">
										<ul id="treeStruttAmm_QUOTE" class="ztree treeStruttAmm"></ul>
									</div>
								</div>
							</s:if>
							<s:else>
								<div class="accordion-heading">
									<span class="accordion-toggle disabled">
										<s:if test="%{strutturaAmministrativoContabile != null}">
											<s:property value="%{strutturaAmministrativoContabile.codice}"/> - <s:property value="%{strutturaAmministrativoContabile.descrizione}"/>
										</s:if>	
										<s:else>
											Nessuna struttura amministrativa selezionata
										</s:else>
									</span>
								</div>
							</s:else>
						</div>
					</div>

					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid_QUOTE" name="strutturaAmministrativoContabile.uid" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice_QUOTE" name="strutturaAmministrativoContabile.codice" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione_QUOTE" name="strutturaAmministrativoContabile.descrizione" />
				</div>
			</div>
		</fieldset> 
		</div>
		<br>
		<h4 class="step-pane"></h4>
		<div class="control-group">
			<label class="control-label" for="commissioniDocumentoSubdocumento">Commissioni *</label>
			<div class="controls">
				<s:select list="listaCommissioniDocumento" name="subdocumento.commissioniDocumento" id="commissioniDocumentoSubdocumento" headerKey="" headerValue="" cssClass="span9" listValue="descrizione" />
			</div>
		</div>
		<div id="accordionAltriDatiSubdocumento" class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#collapseAltriDatiSubdocumento" data-parent="#accordionAltriDatiSubdocumento" data-toggle="collapse" class="accordion-toggle collapsed">
						Altri dati<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div class="accordion-body collapse" id="collapseAltriDatiSubdocumento">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">
							<div class="control-group">
								<label class="control-label">Provvisorio di Cassa</label>
								<div class="controls">
									<span class="alRight">
										<label for="annoProvvisorioCassaSubdocumento" class="radio inline">Anno</label>
									</span>
									<s:textfield id="annoProvvisorioCassaSubdocumento" name="subdocumento.provvisorioCassa.anno" cssClass="lbTextSmall span2 soloNumeri" placeholder="anno" maxlength="4" />
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
									<%-- <span class="alRight<s:if test="%{!etichettaAvvisoVisibile}"> hide</s:if>"> --%>
									<span class="alRight">
										<div class="radio inline collapse_alert">
											<span class="icon-warning-sign icon-red alRight">&nbsp;</span> AVVISO: <s:if test="%{soggetto.avviso}"> SI </s:if><s:else> NO </s:else>
										</div>
									</span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="contoTesoreriaSubdocumento">Conto del tesoriere</label>
								<div class="controls input-append">
									<s:select list="listaContoTesoreria" name="contoTesoreria.uid" id="contoTesoreriaSubdocumento" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span9" />
								</div>
							</div>
							<div class="control-group">
								<label for="causaleOrdinativoSubdocumento" class="control-label">Causale ordinativo *</label>
								<div class="controls">
									<s:textfield id="causaleOrdinativoSubdocumento" name="subdocumento.causaleOrdinativo" cssClass="lbTextSmall span9" required="true" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="noteTesoriereSubdocumento">Note al tesoriere &nbsp;
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
		<div id="accordionDatiCertificazioneCreditoSubdocumento" class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#collapseDatiCertificazioneCreditoSubdocumento" data-parent="#accordionDatiCertificazioneCreditoSubdocumento" data-toggle="collapse" class="accordion-toggle collapsed">
						Dati certificazione del credito<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div class="accordion-body collapse" id="collapseDatiCertificazioneCreditoSubdocumento">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="annotazioneDatiCertificazioneCreditiSubdocumento">Annotazioni</label>
								<div class="controls">
									<s:textarea id="annotazioneDatiCertificazioneCreditiSubdocumento" name="subdocumento.datiCertificazioneCrediti.annotazione" cssClass="span10" rows="3" cols="15" placeholder="annotazione"></s:textarea>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="noteCertificazioneDatiCertificazioneCreditiSubdocumento">Note certificazione</label>
								<div class="controls">
									<s:textfield id="noteCertificazioneDatiCertificazioneCreditiSubdocumento" name="subdocumento.datiCertificazioneCrediti.noteCertificazione" cssClass="span10" placeholder="note" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="numeroCertificazioneCertificazioneDatiCreditiSubdocumento">Dati certificazione</label>
								<div class="controls">
									<s:textfield id="numeroCertificazioneCertificazioneDatiCreditiSubdocumento" name="subdocumento.datiCertificazioneCrediti.numeroCertificazione" cssClass="span1 soloNumeri" placeholder="numero" maxlength="7" />
									<s:textfield id="dataCertificazioneCertificazioneDatiCreditiSubdocumento" name="subdocumento.datiCertificazioneCrediti.dataCertificazione" cssClass="span3 datepicker" placeholder="data" maxlength="10" />
								</div>
							</div>

							<div class="control-group">
								<label for="flagCertificazioneDatiCertificazioneCreditiSubdocumento" class="control-label">Certificabile</label>
								<div class="controls">
									<s:checkbox id="flagCertificazioneDatiCertificazioneCreditiSubdocumento" name="subdocumento.datiCertificazioneCrediti.flagCertificazione" />
								</div>
							</div>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
	</fieldset>

	<p>
		<button type="button" class="btn btn-secondary" id="pulsanteAnnullaInserisciSubdocumento">annulla</button>
		<button type="button" class="btn btn-primary pull-right" id="pulsanteSalvaInserisciSubdocumento">
			salva&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteQuotaSalva"></i></button>
	</p>
</div>
<s:include value="/jsp/movimentoGestione/modaleImpegnoProvvedimento.jsp" />
<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
<s:include value="/jsp/provvedimento/inserisciProvvedimento_modale.jsp" />
