<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${param.suffix != null}">
	<s:set var="suffix">${param.suffix}</s:set>
</c:if>

<c:if test="${param.baseActionName != null}">
  <s:set var="baseActionName">${param.baseActionName}</s:set>
</c:if>

<div id="modaleGuidaCapitolo<s:property value="%{#suffix}" />" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaCapLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Seleziona capitolo</h4>
		</div>

		<div class="modal-body">
			<div class="alert alert-error hide"	id="ERRORI_MODALE_CAPITOLO">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<form>
				<fieldset class="form-horizontal" id="fieldsetRicercaGuidataCapitolo<s:property value="%{#suffix}" />">
					<div id="campiRicercaCapitolo<s:property value="%{#suffix}" />" class="accordion-body collapse in">
						<div class="control-group">
							<label class="control-label" for="annoCapitolo_modale<s:property value="%{#suffix}" />">Anno</label>
							<div class="controls">
								<s:textfield id="annoCapitolo_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.annoCapitolo" disabled="true" value="%{annoEsercizioInt}" data-maintain="" />
								<s:hidden id="annoCapitolo_modaleHidden%{#suffix}" name="modale.capitolo.annoCapitolo" value="%{annoEsercizioInt}" />
								<span class="al">
									<label class="radio inline" for="numeroCapitolo_modale<s:property value="%{#suffix}" />">Capitolo</label>
								</span>
								<s:textfield id="numeroCapitolo_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.numeroCapitolo" />
								<span class="al">
									<label class="radio inline" for="numeroArticolo_modale<s:property value="%{#suffix}" />">Articolo</label>
								</span>
								<s:textfield id="numeroArticolo_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.numeroArticolo" />
								<s:if test="gestioneUEB">
									<span class="al">
										<label class="radio inline" for="numeroUEB_modale<s:property value="%{#suffix}" />">UEB</label>
									</span>
									<s:textfield id="numeroUEB_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.numeroUEB" />
								</s:if>
								<s:else>
									<s:hidden id="numeroUEB_modale%{#suffix}" name="modale.capitolo.numeroUEB" value="1" />
								</s:else>
							</div>
						</div>

						<div class="campiEntrata" id="campiEntrata">
							<div class="control-group">
								<label class="control-label" for="titoloEntrata">Titolo</label>
								<div class="controls">
									<s:select list="listaTitoloEntrata" id="titoloEntrata" cssClass="span12" name="modale.titoloEntrata.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="tipologiaTitolo">Tipologia &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima il Titolo"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima il Titolo</span></i></a></label>
								<div class="controls">
									<select id="tipologiaTitolo" class="span12" disabled="disabled" name="modale.tipologiaTitolo.uid"></select>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="categoriaTipologiaTitolo">Categoria &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima la Tipologia"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima la Tipologia</span></i></a></label>
								<div class="controls">
									<select id="categoriaTipologiaTitolo" name="modale.categoriaTipologiaTitolo.uid" disabled="disabled" class="span12" ></select>
								</div>
							</div>
						</div>

						<div class="control-group">
							<label class="control-label" for="bottonePdC">V Livello P.d.C. finanziario</label>
							<input type="hidden" id="HIDDEN_ElementoPianoDeiContiUid" name="modale.elementoPianoDeiConti.uid" value="${elementoPianoDeiConti.uid}" />
							<div class="controls">
								<div class="accordion span12" class="PDCfinanziario" >
									<div class="accordion-group">
										<div class="accordion-heading">
											<a id="bottonePdC" class="accordion-toggle" data-toggle="collapse" data-parent="#PDCfinanziario" href="">
												<span id="SPAN_ElementoPianoDeiConti">
													Seleziona il conto finanziario
												</span>
												<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>&nbsp;
											</a>
										</div>
										<div id="PDCfin" class="accordion-body collapse">
											<div class="accordion-inner">
												<ul id="treePDC" class="ztree"></ul>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="control-group">
							<label class="control-label">Struttura Amministrativa</label>
							<div class="controls">
								<div class="accordion span12 struttAmm">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#struttAmm_modale<s:property value='%{#suffix}' default='' />" data-toggle="collapse" class="accordion-toggle collapsed">
												<span id="SPAN_StrutturaAmministrativoContabile_modale<s:property value='%{#suffix}' default='' />">Seleziona la Struttura amministrativa</span>
											</a>
										</div>
										<div id="struttAmm_modale<s:property value='%{#suffix}' default='' />" class="accordion-body collapse">
											<div class="accordion-inner">
												<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
												<button type="button" class="btn btn-primary pull-right" data-toggle="collapse" data-target="#struttAmm_modale<s:property value='%{#suffix}' default='' />">Conferma</button>
											</div>
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="modale.strutturaAmministrativoContabile.uid" />
											<s:hidden id="nomeAzioneSAC" value="FCDE" />
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="control-group">
							<span class="al">
								<a class="btn btn-primary pull-right collapsed" id="pulsanteRicercaCapitolo<s:property value="%{#suffix}" />" data-toggle="collapse">
									<i class="icon-search icon"></i>&nbsp;cerca &nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_RicercaCapitolo"></i>
								</a>
							</span>
						</div>
					</div>
					<s:hidden id="HIDDEN_richiediAccantonamentoFondiDubbiaEsigibilita" name="modale.richiediAccantonamentoFondiDubbiaEsigibilita" value="false"/>
			</fieldset>
		</form>

		<div class="hide" id="divRisultatiRicercaCapitolo">
			<div id="buttonSliding" data-toggle="slidewidth"
				class=" fieldset-heading button-sliding"
				data-target="#selezioneConsultazioneEntitaCollegate">
				<h4>
					<span>Elenco capitoli trovati
					</span>
				</h4>
			</div>
			<div>
				<table class="table table-hover" id="risultatiRicercaCapitolo">
					<thead>
						<tr>
							<th scope="col">
								<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti nella pagina corrente" data-referred-table="#risultatiRicercaCapitolo" />
							</th>
							<th scope="col">Capitolo</th>
							<th scope="col">Descrizione</th>
							<th scope="col">Classificazione</th>
							<th scope="col">Struttura</th>
							<th scope="col">Piano dei conti</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<p class="margin-large">
				<button type="button" class="btn" data-dismiss="modal" aria-hidden="true">indietro</button>
				<button type="button" id="pulsanteConfermaCapitoli" class="btn btn-primary" data-submit>conferma</button>
			</p>
			<s:hidden id="HIDDEN_baseActionName" name="baseActionName" />
			<s:form id="formRicercaCapitoli_hidden" cssClass="hide" novalidate="novalidate" action="%{#baseActionName + '_confermaCapitoliModale'}" method="post"></s:form>
		
		</div>
	</div>
</div>
</div>