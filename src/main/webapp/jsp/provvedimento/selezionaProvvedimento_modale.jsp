<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:set var="annoIsNotRequired"><c:out value="${param.annoIsNotRequired}" default="false"/></s:set>

<!--modale provvedimento -->
<div id="modaleGuidaProvvedimento<s:property value='%{suffisso}' default='' />" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">

	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Seleziona provvedimento</h4>

		    
		
		<s:if test="%{#annoIsNotRequired}">
		</s:if>
		<s:else>
			<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p>
		</s:else>
			
		
		</div>

		<div class="modal-body">
			<div class="alert alert-error hide" id="ERRORI_MODALE_PROVVEDIMENTO<s:property value='%{suffisso}' default='' />">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<fieldset class="form-horizontal" id="fieldsetRicercaGuidateProvvedimento<s:property value='%{suffisso}' default='' />">
				<div id="campiRicercaProv<s:property value='%{suffisso}' default='' />" class="accordion-body collapse in">
					<div class="control-group">
					
						<s:if test="%{#annoIsNotRequired}">
							<label class="control-label" for="annoProvvedimento_modale<s:property value='%{suffisso}' default='' />">Anno  </label>
						</s:if>
						<s:else>
							<label class="control-label" for="annoProvvedimento_modale<s:property value='%{suffisso}' default='' />">Anno *</label>
						</s:else>
											
						
						<div class="controls">
							<s:textfield id="%{'annoProvvedimento_modale' + suffisso}" cssClass="span2 soloNumeri" name="modale.attoAmministrativo.anno" maxlength="4" />
							<span class="al">
								<label class="radio inline" for="numeroProvvedimento_modale<s:property value='%{suffisso}' default='' />">Numero</label>
							</span>
							<s:textfield id="%{'numeroProvvedimento_modale' + suffisso}" cssClass="span2 soloNumeri" name="modale.attoAmministrativo.numero" maxlength="7" />
							<span class="al">
								<label class="radio inline" for="tipoAttoProvvedimento_modale<s:property value='%{suffisso}' default='' />">Tipo</label>
							</span>
							<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="modale.tipoAtto.uid" id="%{'tipoAttoProvvedimento_modale' + suffisso}" cssClass="lbTextSmall span3"
								headerKey="" headerValue="" />
							
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">Struttura Amministrativa</label>
						<div class="controls">
							<div class="accordion span9 struttAmm">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#struttAmm_modale<s:property value='%{suffisso}' default='' />" data-toggle="collapse" class="accordion-toggle collapsed">
											<span id="SPAN_StrutturaAmministrativoContabile_modale<s:property value='%{suffisso}' default='' />">Seleziona la Struttura amministrativa</span>
										</a>
									</div>
									<div id="struttAmm_modale<s:property value='%{suffisso}' default='' />" class="accordion-body collapse">
										<div class="accordion-inner">
											<ul id="treeStruttAmm_modale<s:property value='%{suffisso}' default='' />" class="ztree treeStruttAmm"></ul>
											<button type="button" class="btn btn-primary pull-right" data-toggle="collapse" data-target="#struttAmm_modale<s:property value='%{suffisso}' default='' />">Conferma</button>
											<%--<button type="button" class="btn btn-secondary"
													id="BUTTON_deselezionaStrutturaAmministrativoContabile_modale<s:property value='%{suffisso}' default='' />">
												deseleziona
											</button>--%>
										</div>
									</div>
								</div>
							</div>

							<s:hidden id="%{'HIDDEN_StrutturaAmministrativoContabileUid_modale' + suffisso}" name="modale.strutturaAmministrativoContabile.uid" />
							<s:hidden id="%{'HIDDEN_StrutturaAmministrativoContabileCodice_modale' + suffisso}" name="modale.strutturaAmministrativoContabile.codice" />
							<s:hidden id="%{'HIDDEN_StrutturaAmministrativoContabileDescrizione_modale' + suffisso}" name="modale.strutturaAmministrativoContabile.descrizione" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="oggettoProvvedimento_modale<s:property value='%{suffisso}' default='' />">Oggetto</label>
						<div class="controls">
							<s:textfield id="%{'oggettoProvvedimento_modale' + suffisso'}" cssClass="lbTextSmall span9" name="modale.attoAmministrativo.oggetto" maxlength="500" />
						</div>
					</div>
				</div>
				<button type="button" id="pulsanteAnnullaRicercaProvvedimento<s:property value='%{suffisso}' default='' />" class="btn btn-secondary">annulla</button>
				<a class="btn btn-primary pull-right collapsed" href="#" id="pulsanteRicercaProvvedimento<s:property value='%{suffisso}' default='' />" data-toggle="collapse">
					<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_Provvedimento<s:property value='%{suffisso}' default='' />"></i>
				</a>

			</fieldset>
			<div id="divContenitoreTabellaProvvedimento<s:property value='%{suffisso}' default='' />" class="hide">
				<h4>Elenco provvedimenti trovati</h4>
				<table class="table table-hover" id="risultatiRicercaProvvedimento<s:property value='%{suffisso}' default='' />">
					<thead>
						<tr>
							<th></th>
							<th scope="col">Anno</th>
							<th scope="col">Numero</th>
							<th scope="col">Tipo</th>
							<th scope="col">Oggetto</th>
							<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
							<th scope="col">Stato</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
				<a class="btn" href="#" id="pulsanteDeselezionaProvvedimento<s:property value='%{suffisso}' default='' />">deseleziona</a>
			</div>
		</div>
		<div class="modal-footer">
			<a class="btn btn-primary" aria-hidden="true" id="pulsanteConfermaProvvedimento<s:property value='%{suffisso}' default='' />" href="#">conferma</a>
		</div>
	</div>
</div>
<!--/modale provvedimento -->