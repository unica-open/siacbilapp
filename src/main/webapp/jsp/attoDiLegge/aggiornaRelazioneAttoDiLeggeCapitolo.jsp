<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%-- Modale per l'annullamento di un atto di legge --%>
<div id="divAnnullaRelazione" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">

	<div class="modal-body">
		<div id="divAvvisoAnnullamentoRel" class="alert alert-error">
			<p>
				<strong>Attenzione!</strong>
			</p>
			<p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		</div>

		<div id="ERRORI_ANNULLA_REL" class="alert alert-error hide">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>

		<%-- Messaggio di INFORMAZIONI --%>
		<div id="INFORMAZIONI_ANNULLA_REL" class="alert alert-success hide">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>

	</div>

	<div class="modal-footer">
		<button id="btnAnnullaRelChiudi" class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
		<button id="btnAnnullaRelIndietro" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		<button id="btnAnnullaRelProcedi" class="btn btn-primary">si, prosegui</button>
	</div>
</div>
<!--/ Modale Annulla -->


<div class="hide" id="divAggiornaRelazioneAttoDiLeggeCapitolo">
	<h4>Aggiorna relazione</h4>
	<s:form action="aggiornaRelazioneAttoDiLeggeCapitolo" method="post" id="aggiornaRelazioneAttoDiLeggeCapitolo">
		<%-- Messaggio di ERRORI --%>
		<div id="ERRORI_AGGIORNA_REL" class="alert alert-error hide">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>

		<%-- Messaggio di INFORMAZIONI --%>
		<div id="INFORMAZIONI_AGGIORNA_REL" class="alert alert-success hide">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>

		<s:hidden name="relazioneAttoDiLeggeCapitolo.uid" value="" data-maintain="" />
		<s:hidden name="relazioneAttoDiLeggeCapitolo.uidAttoDiLegge" value="" data-maintain="" />
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="fileb1">Gerarchia</label>
				<div class="controls">
					<s:textfield name="relazioneAttoDiLeggeCapitolo.gerarchia" id="fileb1" cssClass="span4" maxlength="200" />
					<%-- Gestione lunghezza --%>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="file_111">Descrizione</label>
				<div class="controls">
					<s:textarea name="relazioneAttoDiLeggeCapitolo.descrizione" id="file_111" cssClass="span4" maxlength="500" />
					<%-- Gestione lunghezza --%>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="file_121">Data inizio finanziamento</label>
				<div class="controls">
					<s:textfield name="relazioneAttoDiLeggeCapitolo.dataInizioFinanziamento" id="file_121" cssClass="span2 datepicker" size="16" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="file_131">Data fine finanziamento</label>
				<div class="controls">
					<s:textfield name="relazioneAttoDiLeggeCapitolo.dataFineFinanziamento" id="file_131" cssClass="span2 datepicker" size="16" />
				</div>
			</div>
		</fieldset>
		<p>
			<a id="btnAggiornaRelChiudi" class="btn hide" data-dismiss="modal" aria-hidden="true">chiudi</a>
			<a id="btnAggiornaRelAnnulla" class="btn btn-link" data-dismiss="res" aria-hidden="true">
				annulla aggiornamento
			</a>
			<a id="btnAggiornaAttoLegge" class="btn" data-target="#aggAtto" data-toggle="modal">
				aggiorna atto di legge
			</a>
			<a id="btnAggiornaRelSalva" class="btn btn-primary hide" href="#">
				salva
			</a>
		</p>
	</s:form>
</div>

<div id="aggAtto" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 id="myModalLabel">Aggiorna Atto di legge</h4>
	</div>
	<div class="modal-body">
		<s:form novalidate="novalidate" action="formAggiornaAttoDiLegge" method="post">
			<s:include value="/jsp/attoDiLegge/aggiornaAttoDiLegge.jsp" />
		</s:form>
	</div>
	<div class="modal-footer">
		<button id="btnModalAggAttoChiudi" class="btn hide" data-dismiss="modal" aria-hidden="true">chiudi</button>
		<button id="btnModalAggAttoAnnulla" class="btn" data-dismiss="modal" aria-hidden="true">annulla aggiornamento</button>
		<a id="btnModalAggAttoSalva" class="btn btn-primary" href="#">salva</a>
	</div>
</div>