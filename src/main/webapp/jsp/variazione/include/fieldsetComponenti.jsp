<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:set name="suffix"><c:out value="${param.suffix}" default="" /></s:set>

<%-- <div id="collapseComponenti<s:property value="#suffix"/>" class="hide"> --%>
	<div class="control-group">
		<br/>
		<button type="button" class="btn pull-left gestisci-collapse" data-selettore-collapse-interno="#collapseNuovaComponente<s:property value="#suffix"/>" id="buttonNuovaComponente<s:property value="#suffix"/>">nuova componente</button>
	</div>
	<div id="collapseNuovaComponente<s:property value="#suffix"/>" class="hide">
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="listaTipoComponente<s:property value="#suffix"/>">Componente</label>
				<div class="controls">
					<s:select list="listaTipoComponente" cssClass="span3" id="listaTipoComponente%{#suffix}" name="tipoComponente.uid"
					headerKey="0" headerValue="" listKey="uid" listValue="%{descrizione}" />
					<span class="al">
						<label class="radio inline" for="tipoComponenteVariazione<s:property value="#suffix"/>">Tipo</label>
					</span>
					<s:textfield id="tipoComponenteVariazione%{#suffix}" data-maintain="" cssClass="lbTextSmall span3" value="Stanziamento" disabled="true"  name="dettaglio.tipoComponente" />
					<%-- una componente nuova non esiste ancora sul capitolo, non la posso eliminare 
					<span class="al">
						<label class="radio inline" for="componenteInVariazioneDaEliminare<s:property value="#suffix"/>">Da eliminare</label>
					</span>
					<input type="checkbox" id="componenteInVariazioneDaEliminare<s:property value="#suffix"/>" name="dettaglio.DaEliminare"/> --%>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoComponenteAnno0InVariazione<s:property value="#suffix"/>"> Importo ${annoEsercizioInt}</label>
				<div class="controls">
					<s:textfield id="importoComponenteAnno0InVariazione%{#suffix}" cssClass="lbTextSmall span2 decimale soloNumeri" disabled="true" name="dettaglio.importo0" />
					<span class="al">
						<label class="radio inline" for="importoComponenteAnno1InVariazione<s:property value="#suffix"/>">Importo ${annoEsercizioInt +1} </label>
					</span>
					<s:textfield id="importoComponenteAnno1InVariazione%{#suffix}" disabled="true" cssClass="lbTextSmall span2 decimale soloNumeri" name="dettaglio.importo" />
					<span class="al">
						<label class="radio inline" for="importoComponenteAnno2InVariazione<s:property value="#suffix"/>">Importo ${annoEsercizioInt +2} </label>
					</span>
					<s:textfield id="importoComponenteAnno2InVariazione%{#suffix}" disabled="true" cssClass="lbTextSmall span2 decimale soloNumeri" name="dettaglio.importo" />
					&nbsp; <button type="button" class="btn pull-right" id="button_salvaNuovaComponente<s:property value="#suffix"/>">aggiungi componente</button><%-- Gestione della chiamata AJAX --%>
				</div>
			</div>
		</fieldset>
	</div>
	<div class="clear"></div>
	<div class="span11">
		<table class="table table-hover table-bordered table-condensed tab_left" id="tabellaStanziamentiTotaliPerComponente<s:property value="#suffix"/>">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>&nbsp;</th>
					<th class="span3"><abbr title="componente da eliminare sul capitolo">Da eliminare sul capitolo</abbr></th>
					<th class="span3">Importo ${annoEsercizioInt}</th>
					<th class="span3">Importo ${annoEsercizioInt +1}</th>
					<th class="span3">Importo ${annoEsercizioInt +2}</th>
					<th class="tab_Right span1">&nbsp;</th>
					<th class="tab_Right span1">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
<!-- </div> -->